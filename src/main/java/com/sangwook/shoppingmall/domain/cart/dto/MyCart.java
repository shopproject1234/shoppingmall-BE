package com.sangwook.shoppingmall.domain.cart.dto;

import com.sangwook.shoppingmall.domain.cart.Cart;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyCart {

    private Long itemId;
    private Integer itemCount;
    private String itemName;
    private Integer totalPrice;

    public MyCart(Cart cart) {
        this.itemId = cart.getItem().getId();
        this.itemCount = cart.getCount();
        this.itemName = cart.getItem().getName();
        this.totalPrice = cart.getPrice() * cart.getCount();
    }
}
