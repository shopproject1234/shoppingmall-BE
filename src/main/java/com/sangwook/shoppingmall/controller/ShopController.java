package com.sangwook.shoppingmall.controller;

import com.sangwook.shoppingmall.argumentResolver.Login;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ShopController {

    private final ItemService itemService;

    @PostMapping("/item/upload")
    public void addItem(@Login User user, @RequestBody AddItem addItem) {
        itemService.add(user, addItem);
    }
}
