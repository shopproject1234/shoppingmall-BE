package com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddCart {

    private Long itemId;
    private Integer itemCount;
}
