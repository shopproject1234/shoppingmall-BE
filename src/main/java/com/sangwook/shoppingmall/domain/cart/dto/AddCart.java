package com.sangwook.shoppingmall.domain.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCart {

    private Long itemId;
    private Integer itemCount;
}
