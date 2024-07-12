package com.example.test_service.controller;

import com.example.test_service.dto.ApiResponse;
import com.example.test_service.dto.request.CreateTest;
import com.example.test_service.dto.request.EditTest;
import com.example.test_service.dto.request.SubmitTest;
import com.example.test_service.dto.response.TestDTO;
import com.example.test_service.dto.response.TestDTOResponse;
import com.example.test_service.dto.response.TestDetail;
import com.example.test_service.dto.response.TestUserDTO;
import com.example.test_service.service.TestService;
import com.example.test_service.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;
    private final JwtUtils jwtUtils;

    @GetMapping("/by-topic/{topicId}")
    ResponseEntity<ApiResponse<TestDTOResponse>> getByTopic(@PathVariable("topicId") String topicId) {
        String userId = jwtUtils.getUserIdFromJwt();
        TestDTOResponse testDTO = testService.getByTopic(topicId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(1011, "Successfully", testDTO)
        );
    }

    @PostMapping("/check")
    ResponseEntity<ApiResponse<Boolean>> check(@RequestBody List<String> topicIds) {
        String userId = jwtUtils.getUserIdFromJwt();
        Boolean testDTO = testService.check(topicIds, userId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(1011, "Successfully", testDTO)
        );
    }

    @GetMapping("/detail/{id}")
    ResponseEntity<ApiResponse<TestDetail>> getDetailTest(@PathVariable("id") String id) {
        String userId = jwtUtils.getUserIdFromJwt();
        TestDetail testDetail = testService.getdetailTest(id, userId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(1011, "Successfully", testDetail)
        );
    }

    @PostMapping("/detail/{id}")
    ResponseEntity<ApiResponse<String>> submitTest(
            @RequestBody SubmitTest submitTest,
            @PathVariable("id") String id
    ) {
        String userId = jwtUtils.getUserIdFromJwt();
        String code = testService.submitTest(id, userId, submitTest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(1011, "Successfully", code)
        );
    }

    @GetMapping("/result/{id}")
    ResponseEntity<ApiResponse<TestUserDTO>> getResultTest(@PathVariable("id") String id) {
        String userId = jwtUtils.getUserIdFromJwt();
        TestUserDTO testUserDTO = testService.getresultTest(id, userId);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(1011, "Successfully", testUserDTO)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/insert")
    ResponseEntity<ApiResponse<TestDTO>> save(CreateTest createTest) {
        String userId = jwtUtils.getUserIdFromJwt();
        TestDTO testDTO = testService.saveTest(userId, createTest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(1011, "Successfully", testDTO)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit")
    ResponseEntity<ApiResponse<TestDTO>> edit(EditTest editTest) {
        String userId = jwtUtils.getUserIdFromJwt();
        TestDTO testDTO = testService.editTest(userId, editTest);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(1011, "Successfully", testDTO)
        );
    }
}
