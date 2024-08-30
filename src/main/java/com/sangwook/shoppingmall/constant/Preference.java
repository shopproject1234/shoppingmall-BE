package com.sangwook.shoppingmall.constant;

public enum Preference {
    NONE(0),
    SEARCHED(25),
    PURCHASED(60),
    INTERESTED(90);

    private final Integer score;

    Preference(Integer score) {
        this.score = score;
    }
}
