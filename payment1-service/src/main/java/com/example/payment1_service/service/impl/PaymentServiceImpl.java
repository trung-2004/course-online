package com.example.payment1_service.service.impl;

import com.example.payment1_service.configuration.VNPAYConfig;
import com.example.payment1_service.dto.ApiResponse;
import com.example.payment1_service.dto.request.CreateItemOnlineUser;
import com.example.payment1_service.dto.request.CreateOrderRequest;
import com.example.payment1_service.dto.request.CreatePayment;
//import com.example.payment1_service.dto.request.CreatePaymentRequest;
import com.example.payment1_service.dto.request.TestUserRequest;
import com.example.payment1_service.dto.response.*;
import com.example.payment1_service.entity.Payment;
import com.example.payment1_service.entity.Status;
import com.example.payment1_service.exception.AppException;
import com.example.payment1_service.exception.ErrorCode;
import com.example.payment1_service.mapper.PaymentMapper;
import com.example.payment1_service.repository.PaymentRepository;
import com.example.payment1_service.repository.httpclient.CourseClient;
import com.example.payment1_service.repository.httpclient.IdentityClient;
import com.example.payment1_service.repository.httpclient.PaymentClient;
import com.example.payment1_service.service.PaymentService;
import com.example.payment1_service.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final IdentityClient identityClient;
    private final CourseClient courseClient;
    private final PaymentClient paymentClient;
    private final PaymentMapper paymentMapper;
    private final VNPAYConfig vnPayConfig;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PaymentDTO buyCourse(String id, CreatePayment request) {
        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .userId(id)
                .paymentMethod(request.getPaymentMethod())
                .status(0)
                .createdBy("Demo")
                .modifiedBy("Demo")
                .createdDate(new Timestamp(System.currentTimeMillis()))
                .modifiedDate(new Timestamp(System.currentTimeMillis()))
                .build();
        paymentRepository.save(payment);
        return paymentMapper.toPaymentDTO(payment);
    }

    @Transactional(rollbackFor = Exception.class)
    public PaymentDTO1.VNPayResponse createVnPayPayment(CreatePayment request) {
        long amount = (long) (request.getAmount() * 23000 * 100L);
        String bankCode = request.getBankCode();

        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        //vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        String ipAddress = VNPayUtil.getIpAddress();
        vnpParamsMap.put("vnp_IpAddr", ipAddress);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
        log.info("created_date: {}", vnpCreateDate);
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);

        // put orderId,...
        vnpParamsMap.put("vnp_OrderInfo", "Order " + request.getOrderId());
        vnpParamsMap.put("vnp_TxnRef", String.valueOf(request.getOrderId()));
        //vnpParamsMap.put("vnp_OrderId", request.getOrderId());

        //build query url
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;


        return PaymentDTO1.VNPayResponse.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();
    }

    @Override
    public PaymentDTO1.VNPayResponse returnVnPay(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        String orderId = request.getParameter("vnp_TxnRef");
        log.info("OrderId: {}", orderId);
        if (status.equals("00")) {
            // change status payment
            Payment payment = paymentRepository.findByOrderId(orderId);
            payment.setStatus(1);
            paymentRepository.save(payment);

            ApiResponse<CourseUserDTO> courseUserDTOApiResponse = paymentClient.getCouseUser(orderId);
            if (courseUserDTOApiResponse.getResult() == null) {
                log.info("Course User not found");
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            }

            CreateItemOnlineUser itemOnlineUser = CreateItemOnlineUser.builder()
                    .courseId(courseUserDTOApiResponse.getResult().getCourseId())
                    .userId(payment.getUserId())
                    .build();

            ApiResponse<UserResponse> response = identityClient.getUser(courseUserDTOApiResponse.getResult().getUserId());
            if (response.getResult() == null) {
                log.info("User not found");
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            }

            ApiResponse<CourseResponse> responseApiCourse = courseClient.getCourseById(courseUserDTOApiResponse.getResult().getCourseId());
            if (responseApiCourse.getResult() == null) {
                log.info("Course not found");
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            }

            MessageDTO messageDTO = MessageDTO.builder()
                    .to(response.getResult().getUsername())
                    .toName(response.getResult().getUsername())
                    .subject("Welcome to English Academy")
                    .content("English Academy is online learning flatform")
                    .build();

            CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
                    .orderId(orderId)
                    .status(1)
                    .build();

            TestUserRequest testUserRequest = TestUserRequest.builder()
                    .topicId(responseApiCourse.getResult().getTopicIds())
                    .userId(payment.getUserId())
                    .build();

            kafkaTemplate.send("topicItemOnline", itemOnlineUser);
            kafkaTemplate.send("notification", messageDTO);
            kafkaTemplate.send("orderStatus1", createOrderRequest);
            kafkaTemplate.send("testUser", testUserRequest);

            return new PaymentDTO1.VNPayResponse("00", "Success", "");
        } else {
            // change status payment

            Payment payment = paymentRepository.findByOrderId(orderId);
            payment.setStatus(2);
            paymentRepository.save(payment);


            CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
                    .orderId(orderId)
                    .status(2)
                    .build();
            kafkaTemplate.send("orderStatus1", createOrderRequest);

            return new PaymentDTO1.VNPayResponse(status, "False", "");
        }
    }


}
