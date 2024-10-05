package com.sangwook.shoppingmall.exception.custom;

import lombok.Getter;

@Getter
public class ObjectNotFoundException extends RuntimeException {

    private final String defaultMessage = "대상을 찾을 수 없습니다";
    private final String message;
    private final String method;

    public ObjectNotFoundException(String message, String method) {
        this.message = message;
        this.method = method;
    }

    public ObjectNotFoundException(String method) {
        this.message = defaultMessage;
        this.method = method;
    }
}
