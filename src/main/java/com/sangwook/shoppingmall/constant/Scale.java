package com.sangwook.shoppingmall.constant;

import lombok.Getter;

@Getter
public enum Scale {
    SEARCH(1),
    INFO(2),
    CART(5),
    ORDER(10);

    private final Integer value;

    Scale(Integer value) {
        this.value = value;
    }
}
