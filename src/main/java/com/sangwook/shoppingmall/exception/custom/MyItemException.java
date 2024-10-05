package com.sangwook.shoppingmall.exception.custom;

import lombok.Getter;

@Getter
public class MyItemException extends RuntimeException {

    private final String message;
    private final String method;

    public MyItemException(String message, String method) {
        this.message = message;
        this.method = method;
    }
}
