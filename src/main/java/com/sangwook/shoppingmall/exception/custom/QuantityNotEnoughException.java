package com.sangwook.shoppingmall.exception.custom;

import lombok.Getter;

@Getter
public class QuantityNotEnoughException extends RuntimeException {

    private final String defaultMessage = "수량이 부족합니다";
    private final String message;
    private final String method;

    public QuantityNotEnoughException(String message, String method) {
        this.message = message;
        this.method = method;
    }

    public QuantityNotEnoughException(String method) {
        this.message = defaultMessage;
        this.method = method;
    }
}
