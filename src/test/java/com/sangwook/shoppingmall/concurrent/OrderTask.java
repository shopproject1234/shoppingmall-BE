package com.sangwook.shoppingmall.concurrent;

import com.sangwook.shoppingmall.entity.cartaggregate.cart.application.CartService;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.AddCart;

public class OrderTask implements Runnable {

    CartService cartService;
    Long customerId;

    public OrderTask(CartService cartService, Long customerId) {
        this.cartService = cartService;
        this.customerId = customerId;
    }

    @Override
    public void run() {
        cartService.order(customerId);
    }
}
