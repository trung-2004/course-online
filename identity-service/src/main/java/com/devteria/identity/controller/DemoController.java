package com.devteria.identity.controller;

import com.devteria.identity.dto.request.ApiResponse;
import com.devteria.identity.dto.request.AuthenticationRequest;
import com.devteria.identity.dto.request.DemoRequest;
import com.devteria.identity.dto.response.AuthenticationResponse;
import com.devteria.identity.service.DemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {
    private final DemoService demoService;

    @PostMapping("/post-rest")
    ApiResponse<String> postRest(@RequestBody DemoRequest request) {
        String result = demoService.postRest(request);
        return ApiResponse.<String>builder().result(result).build();
    }

    @GetMapping("/get-rest/{name}")
    ApiResponse<String> getRest(@PathVariable("name") String request) {
        String result = demoService.getRest(request);
        return ApiResponse.<String>builder().result(result).build();
    }

    @PostMapping("/post-web")
    ApiResponse<String> postWeb(@RequestBody DemoRequest request) {
        String result = demoService.postWeb(request);
        return ApiResponse.<String>builder().result(result).build();
    }

    @GetMapping("/get-web/{name}")
    ApiResponse<String> getWeb(@PathVariable("name") String request) {
        String result = demoService.getWeb(request);
        return ApiResponse.<String>builder().result(result).build();
    }

    @PostMapping("/post-feign")
    ApiResponse<String> postfeign(@RequestBody DemoRequest request) {
        String result = demoService.postfeign(request);
        return ApiResponse.<String>builder().result(result).build();
    }

    @GetMapping("/get-feign/{name}")
    ApiResponse<String> getfeign(@PathVariable("name") String request) {
        String result = demoService.getfeign(request);
        return ApiResponse.<String>builder().result(result).build();
    }
}
