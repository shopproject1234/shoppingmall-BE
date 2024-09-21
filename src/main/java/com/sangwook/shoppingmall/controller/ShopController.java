package com.sangwook.shoppingmall.controller;

import com.sangwook.shoppingmall.argumentResolver.Login;
import com.sangwook.shoppingmall.domain.cart.dto.AddCart;
import com.sangwook.shoppingmall.domain.cart.dto.DeleteCart;
import com.sangwook.shoppingmall.domain.cart.dto.MyCart;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.item.dto.ItemInfo;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.service.CartService;
import com.sangwook.shoppingmall.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ShopController {

    private final ItemService itemService;
    private final CartService cartService;

    /**
     *  상품
     */
    @PostMapping("/item/upload")
    public void addItem(@Login User user, @RequestBody AddItem addItem) {
        itemService.add(user, addItem);
    }

    @DeleteMapping("/item/{item_id}/delete")
    public void deleteItem(@Login User user, @PathVariable("item_id") Long itemId) {
        itemService.delete(user, itemId);
    }

    @GetMapping("/item/{item_id}/info")
    public ItemInfo itemInfo(@PathVariable("item_id") Long itemId) {
        return itemService.getInfo(itemId);
    }

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
