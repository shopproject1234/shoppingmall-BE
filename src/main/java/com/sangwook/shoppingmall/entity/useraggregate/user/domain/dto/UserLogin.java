package com.sangwook.shoppingmall.entity.useraggregate.user.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLogin {

    @NotNull
    private String email;

    @NotNull
    private String password;
}
