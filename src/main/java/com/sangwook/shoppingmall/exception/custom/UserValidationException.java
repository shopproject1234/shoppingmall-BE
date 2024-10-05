package com.sangwook.shoppingmall.exception.custom;

import lombok.Getter;

@Getter
public class UserValidationException extends RuntimeException{

    private final String defaultMessage = "유저 검증에 실패했습니다";
    private final String message;
    private final String method;

    public UserValidationException(String message, String method) {
        this.message = message;
        this.method = method;
    }

    public UserValidationException(String method) {
        this.message = defaultMessage;
        this.method = method;
    }
}
