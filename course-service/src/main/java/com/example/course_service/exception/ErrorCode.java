package com.example.course_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(400, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(400, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(400, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(400, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(400, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(404, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(401, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(400, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    // certificate
    CERTIFICATE_QUALIFIED(400, "You are not qualified yet", HttpStatus.BAD_REQUEST),
    CERTIFICATE_NOTFOUND(404, "Certificate not found", HttpStatus.NOT_FOUND),

    // course
    COURSE_NOTFOUND(404, "Course not found", HttpStatus.NOT_FOUND),
    COURSE_EXISTED(404, "Course existed", HttpStatus.NOT_FOUND),

    // topic
    TOPIC_NOTFOUND(404, "Topic not found", HttpStatus.NOT_FOUND),
    TOPIC_EXISTED(404, "Topic existed", HttpStatus.NOT_FOUND),
    // item online
    ITEMONLINE_NOTFOUND(404, "Item Online not found", HttpStatus.NOT_FOUND),
    ITEMONLINE_EXISTED(404, "Item Online existed", HttpStatus.NOT_FOUND),

    // category

    CATEGORY_NOTFOUND(404, "Category not found", HttpStatus.NOT_FOUND),

    // profile
    PROFILE_UPDATE(404, "Please update your profile", HttpStatus.NOT_FOUND),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
