package com.sangwook.shoppingmall.entity.useraggregate.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailCheck {

    private String email;
    private Integer code;
}
