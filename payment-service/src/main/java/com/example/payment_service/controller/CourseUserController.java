package com.example.payment_service.controller;

import com.example.payment_service.dto.ApiResponse;
import com.example.payment_service.dto.request.CreateCourseUser;
import com.example.payment_service.dto.response.CourseUserDTO;
import com.example.payment_service.dto.response.PaymentDTO1;
import com.example.payment_service.service.CourseUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CourseUserController {
    private final CourseUserService courseUserService;

    @GetMapping("/get-by-user")
    public ResponseEntity<ApiResponse<List<String>>> getAllIdByUser(){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = jwt.getClaimAsString("userId");
        List<String> list = courseUserService.getIdCourseByUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", list)
        );
    }

    @GetMapping("/any/course-user/{id}")
    public ResponseEntity<ApiResponse<CourseUserDTO>> getIdByUser(@PathVariable("id") String id){
        CourseUserDTO list = courseUserService.getCourseUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", list)
        );
    }

    @GetMapping("/check-buy-course/{courseId}")
    public ResponseEntity<ApiResponse<Boolean>> getAllIdByUser(@PathVariable("courseId") String courseId){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = jwt.getClaimAsString("userId");
        Boolean aBoolean = courseUserService.checkBuyCourse(courseId, id);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", aBoolean)
        );
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/buy-course")
    public ResponseEntity<ApiResponse<PaymentDTO1.VNPayResponse>> buyCourse(@RequestBody @Valid CreateCourseUser request){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = jwt.getClaimAsString("userId");
        PaymentDTO1.VNPayResponse courseUserDTO = courseUserService.buyCourse(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", courseUserDTO)
        );
    }

    @PutMapping("/start-course/{courseId}")
    public ResponseEntity<ApiResponse> startCourse(@PathVariable("courseId") String courseId){
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String id = jwt.getClaimAsString("userId");
        courseUserService.startCourse(id, courseId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(200, "Successfully", "")
        );
    }

//    @GetMapping("/any/return-vnpay")
//    public ResponseEntity<ApiResponse<PaymentDTO1.VNPayResponse>> returnVNPAY(HttpServletRequest request){
//        PaymentDTO1.VNPayResponse list = courseUserService.returnVnPay(request);
//        return ResponseEntity.status(HttpStatus.OK).body(
//                new ApiResponse<>(200, "Successfully", list)
//        );
//    }

}
