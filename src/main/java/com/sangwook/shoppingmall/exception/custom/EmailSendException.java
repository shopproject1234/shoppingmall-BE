package com.sangwook.shoppingmall.exception.custom;

import lombok.Getter;

@Getter
public class EmailSendException extends RuntimeException {

    private final String message;
    private final String method;

    public EmailSendException(String message, String method) {
        this.message = message;
        this.method = method;
    }
}
