package com.sangwook.shoppingmall.entity.cartaggregate.cart.application;

import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.Cart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.AddCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.DeleteCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.MyCart;

import java.util.List;

public interface CartService {

    Cart add(Long userId, AddCart addCart);
    void delete(Long userId, DeleteCart deleteCart);
    List<MyCart> getMyCart(Long userId);
    void order(Long userId);
}
