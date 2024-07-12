package com.example.payment_service.exception;

import com.example.payment_service.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;

public class CustomErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.NOT_FOUND.value()) {
            try (InputStream bodyIs = response.body().asInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                ApiResponse apiResponse = mapper.readValue(bodyIs, ApiResponse.class);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, apiResponse.getMessage());
            } catch (IOException e) {
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
            }
        }
        return defaultErrorDecoder.decode(methodKey, response);
    }
}
