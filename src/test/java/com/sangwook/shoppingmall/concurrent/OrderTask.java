package com.sangwook.shoppingmall.concurrent;

import com.sangwook.shoppingmall.entity.cartaggregate.cart.application.CartService;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.AddCart;

public class OrderTask implements Runnable {

    CartService cartService;
    Long customerId;
    AddCart addCart;

    public OrderTask(CartService cartService, Long customerId, AddCart addCart) {
        this.cartService = cartService;
        this.customerId = customerId;
        this.addCart = addCart;
    }

    @Override
    public void run() {
        cartService.order(customerId);
    }
}
