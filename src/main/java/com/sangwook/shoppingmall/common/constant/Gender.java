package com.sangwook.shoppingmall.common.constant;

import lombok.Getter;

@Getter
public enum Gender {
    MALE("남성"),
    FEMALE("여성");

    private final String displayName;

    Gender(String displayName) {
        this.displayName = displayName;
    }

}
