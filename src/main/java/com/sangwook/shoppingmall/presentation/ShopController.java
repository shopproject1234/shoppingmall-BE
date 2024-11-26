package com.sangwook.shoppingmall.presentation;

import com.sangwook.shoppingmall.common.argumentResolver.Login;
import com.sangwook.shoppingmall.application.CartService;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.dto.AddCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.dto.DeleteCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.dto.MyCart;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ShopController {

    private final CartService cartService;

    /**
     *  장바구니
     */
    @PostMapping("/cart/{user_id}/upload")
    public void addCart(@PathVariable("user_id") Long userId, @Login User user, @RequestBody AddCart addCart) {
        checkUser(user, userId);
        cartService.add(userId, addCart);
    }

    @DeleteMapping("/cart/{user_id}/delete")
    public void deleteCart(@PathVariable("user_id") Long userId, @Login User user, @RequestBody DeleteCart deleteCart) {
        checkUser(user, userId);
        cartService.delete(userId, deleteCart);
    }

    @GetMapping("/cart/{user_id}")
    public List<MyCart> getCart(@PathVariable("user_id") Long userId, @Login User user) {
        checkUser(user, userId);
        return cartService.getMyCart(userId);
    }

    @PostMapping("/cart/{user_id}/order")
    public void order(@PathVariable("user_id") Long userId, @Login User user) {
        checkUser(user, userId);
        cartService.order(userId);
    }

    private void checkUser(User user, Long userId) {
        if (!user.getId().equals(userId)) {
            throw new IllegalStateException();
        }
    }
}
