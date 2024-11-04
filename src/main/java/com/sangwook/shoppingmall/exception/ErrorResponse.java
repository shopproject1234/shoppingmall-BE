package com.sangwook.shoppingmall.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {

    private LocalDateTime timestamp;
    private String message;
    private String path;

    public static ErrorResponse error(RuntimeException e, String path) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(e.getMessage());
        errorResponse.setPath(path);
        errorResponse.setTimestamp(LocalDateTime.now());
        return errorResponse;
    }

    public static ErrorResponse error(String message) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage(message);
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(null);
        return errorResponse;
    }

}
