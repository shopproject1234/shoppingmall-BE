package com.sangwook.shoppingmall.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLogin {

    @NotNull
    private String email;

    @NotNull
    private String password;
}
