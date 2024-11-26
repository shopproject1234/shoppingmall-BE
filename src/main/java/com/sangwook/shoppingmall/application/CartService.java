package com.sangwook.shoppingmall.application;

import com.sangwook.shoppingmall.entity.cartaggregate.domain.Cart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.dto.AddCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.dto.DeleteCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.dto.MyCart;

import java.util.List;

public interface CartService {

    Cart add(Long userId, AddCart addCart);
    void delete(Long userId, DeleteCart deleteCart);
    List<MyCart> getMyCart(Long userId);
    void order(Long userId);
}
