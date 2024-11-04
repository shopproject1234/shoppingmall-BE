package com.sangwook.shoppingmall.exception.custom;

import lombok.Getter;

@Getter
public class ObjectAlreadyExistException extends RuntimeException {

    private final String defaultMessage = "대상이 이미 존재합니다";
    private final String message;
    private final String method;

    public ObjectAlreadyExistException(String message, String method) {
        this.message = message;
        this.method = method;
    }

    public ObjectAlreadyExistException(String method) {
        this.message = defaultMessage;
        this.method = method;
    }
}
