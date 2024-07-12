package com.example.test_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(404, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1009, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),

    // course
    COURSE_NOTFOUND(404, "Course not found", HttpStatus.NOT_FOUND),
    COURSE_EXISTED(404, "Course existed", HttpStatus.NOT_FOUND),

    // topic
    TOPIC_NOTFOUND(404, "Topic not found", HttpStatus.NOT_FOUND),
    TOPIC_EXISTED(404, "Topic existed", HttpStatus.NOT_FOUND),

    // category

    CATEGORY_NOTFOUND(404, "Category not found", HttpStatus.NOT_FOUND),

    // category

    QUESTION_NOTFOUND(404, "Question not found", HttpStatus.NOT_FOUND),

    // test
    TEST_NOTFOUND(404, "Test not found", HttpStatus.NOT_FOUND),
    TEST_USER_HAS_ALREADY_ATTEMPTED_TODAY(400, "USER HAS ALREADY ATTEMPTED TODAY", HttpStatus.BAD_REQUEST),
    QUESTION_EASY_NOT_ENOUGH(400, "The number of easy questions is not enough, the exam cannot be created", HttpStatus.BAD_REQUEST),
    QUESTION_MEDIUM_NOT_ENOUGH(400, "The number of medium questions is not enough, the exam cannot be created", HttpStatus.BAD_REQUEST),
    QUESTION_HARD_NOT_ENOUGH(400, "The number of hard questions is not enough, the exam cannot be created", HttpStatus.BAD_REQUEST),



    NOTFOUND(404, "Not found", HttpStatus.NOT_FOUND),
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
