package com.sangwook.shoppingmall.concurrent;

import com.sangwook.shoppingmall.application.CartService;

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
