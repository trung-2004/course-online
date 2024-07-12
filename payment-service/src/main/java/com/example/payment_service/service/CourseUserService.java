package com.example.payment_service.service;

import com.example.payment_service.dto.ApiResponse;
import com.example.payment_service.dto.request.CreateCourseUser;
import com.example.payment_service.dto.request.CreateItemOnlineUser;
import com.example.payment_service.dto.request.CreatePaymentRequest;
import com.example.payment_service.dto.request.PaymentRequest;
import com.example.payment_service.dto.response.*;
import com.example.payment_service.entity.CourseUser;
import com.example.payment_service.entity.Status;
import com.example.payment_service.exception.AppException;
import com.example.payment_service.exception.ErrorCode;
import com.example.payment_service.mapper.CourseUserMapper;
import com.example.payment_service.repository.CourseUserRepository;
import com.example.payment_service.repository.httpclient.CourseClient;
import com.example.payment_service.repository.httpclient.IdentityClient;
import com.example.payment_service.repository.httpclient.Payment1Client;
import com.example.payment_service.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseUserService {
    private final CourseUserRepository courseUserRepository;
    private final CourseClient courseClient;
    private final Payment1Client payment1Client;
    private final IdentityClient identityClient;
    private final CourseUserMapper courseUserMapper;
    private final JwtUtils jwtUtils;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public List<String> getIdCourseByUser(String id) {
        return courseUserRepository.findCourseIdsByUserId(id);
    }

    @Transactional
    public PaymentDTO1.VNPayResponse buyCourse(String id, CreateCourseUser request) {
        try {
            Date now = new Timestamp(System.currentTimeMillis());
            ApiResponse<CourseResponse> courseResponse = courseClient.getCourseById(request.getCourseId());
            if (courseResponse.getResult() == null) {
                log.info("Course not found");
                throw new AppException(ErrorCode.COURSE_NOTFOUND);
            }
            List<CourseUser> courseUserExisting = courseUserRepository.findAllByCourseIdAndUserIdAndStatus(request.getCourseId(), id, 1);
            for (CourseUser courseUser : courseUserExisting) {
                if (courseUser != null && courseUser.getExpiryDate() != null && now.before(courseUser.getExpiryDate())) {
                    throw new AppException(ErrorCode.COURSE_EXISTED);
                }
            }

            CourseUser courseUser = CourseUser.builder()
                    .courseId(request.getCourseId())
                    .userId(id)
                    .status(0)
                    .price(courseResponse.getResult().getPrice())
                    .paymentMethod(request.getPaymentMethod())
                    .createdBy(jwtUtils.getUsernameFromToken())
                    .modifiedBy(jwtUtils.getUsernameFromToken())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .modifiedDate(new Timestamp(System.currentTimeMillis()))
                    .build();
            courseUserRepository.save(courseUser);

            PaymentRequest paymentRequest = PaymentRequest.builder()
                    .amount(courseResponse.getResult().getPrice())
                    .orderId(courseUser.getId())
                    .paymentMethod(courseUser.getPaymentMethod())
                    .bankCode(request.getBankCode())
                    .build();
            ApiResponse<PaymentDTO1.VNPayResponse> paymentResponse = payment1Client.creatPayment(paymentRequest);
            if (courseResponse.getResult() == null) {
                log.info("Payment false");
                courseUser.setStatus(2);
                courseUserRepository.save(courseUser);
                throw new AppException(ErrorCode.COURSE_NOTFOUND);
            }
            return paymentResponse.getResult();
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new AppException(ErrorCode.COURSE_NOTFOUND);
            }
            throw e; // Rethrow other exceptions
        } catch (AppException e) {
            // Xử lý các ngoại lệ từ ứng dụng
            log.error("Error during buy course process", e);
            throw e; // Re-throw custom exceptions to trigger rollback
        } catch (Exception e) {
            // Xử lý các ngoại lệ khác
            log.error("Unexpected error during buy course process", e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public Boolean checkBuyCourse(String courseId, String id) {
        Date now = new Timestamp(System.currentTimeMillis());
        List<CourseUser> courseUserList = courseUserRepository.findAllByCourseIdAndUserIdAndStatus(courseId, id, 1);

        // Kiểm tra xem danh sách courseUserList có bản ghi nào hợp lệ không
        for (CourseUser courseUser : courseUserList) {
            if (courseUser != null && courseUser.getExpiryDate() != null && courseUser.getPurchaseDate() != null) {
                // Nếu now nằm giữa purchaseDate và expiryDate thì khóa học còn hiệu lực
                if (now.after(courseUser.getPurchaseDate()) && now.before(courseUser.getExpiryDate())) {
                    return true;
                }
            }
        }

        // Nếu không có bản ghi nào hợp lệ trong danh sách
        return false;
    }


    public void startCourse(String id, String courseId) {
        Date now = new Date(System.currentTimeMillis());

        List<CourseUser> courseUser = courseUserRepository.findAllByCourseIdAndUserIdAndStatus(courseId, id, 1);
        if (courseUser.size() == 0) return;
        for (CourseUser courseUser1 : courseUser) {
            if (courseUser1.getExpiryDate() == null || now.after(courseUser1.getExpiryDate())){
                courseUser1.setPurchaseDate(now);
                courseUser1.setExpiryDate(Date.from(LocalDateTime.now().plusMonths(3).atZone(ZoneId.systemDefault()).toInstant()));
                courseUserRepository.save(courseUser1);
            }
        }

    }

    public CourseUserDTO getCourseUserById(String id) {
        CourseUser courseUser = courseUserRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND));
        return courseUserMapper.toCourseUserDTO(courseUser);
    }

    @Transactional
    @KafkaListener(id = "orderStatusGroup1", topics = "orderStatus1")
    public void listen(CreatePaymentRequest request){
        try {
            log.info("Received: {}", request.getOrderId());
            CourseUser courseUser = courseUserRepository.findById(request.getOrderId()).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOTFOUND));
            courseUser.setStatus(request.getStatus());
            courseUserRepository.save(courseUser);
        } catch (Exception e) {
            log.error("Error processing itemOnlineUser: {}", e);
            // Optionally, you can rethrow the exception or handle it accordingly
            // throw e;
        }

    }
}
