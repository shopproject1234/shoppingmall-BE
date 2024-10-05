package com.sangwook.shoppingmall.exception.custom;

import lombok.Getter;

@Getter
public class SessionNotFoundException extends RuntimeException {

    private final String message;
    private final String method;

    public SessionNotFoundException(String message, String method) {
        this.message = message;
        this.method = method;
    }
}
