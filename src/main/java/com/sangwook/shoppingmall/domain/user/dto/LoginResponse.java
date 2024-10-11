package com.sangwook.shoppingmall.domain.user.dto;

import lombok.Getter;

@Getter
public class LoginResponse {

    private Long userId;
    private String nickname;

    public LoginResponse(Long userId, String nickname) {
        this.userId = userId;
        this.nickname = nickname;
    }
}
