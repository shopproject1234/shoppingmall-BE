package com.sangwook.shoppingmall.presentation;

import com.sangwook.shoppingmall.common.argumentResolver.Login;
import com.sangwook.shoppingmall.application.ItemService;
import com.sangwook.shoppingmall.common.argumentResolver.UserForScale;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.AddItem;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.ItemInfo;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.ItemList;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    /**
     *  상품
     */
    @PostMapping("/item/upload")
    public void addItem(@Login User user, @RequestBody AddItem addItem) {
        itemService.add(user, addItem);
    }

    @PatchMapping("/item/{item_id}/update")
    public void updateItem(@PathVariable("item_id") Long itemId, @Login User user, @RequestBody AddItem addItem) {
        itemService.update(user, itemId, addItem);
    }

    @DeleteMapping("/item/{item_id}/delete")
    public void deleteItem(@Login User user, @PathVariable("item_id") Long itemId) {
        itemService.delete(user, itemId);
    }

    @GetMapping("/item/{item_id}/info")
    public ItemInfo itemInfo(@PathVariable("item_id") Long itemId, @UserForScale User user) {
        return itemService.getInfo(itemId, user);
    }

    @GetMapping("/item/list")
    public Page<ItemList> itemList(@RequestParam int page,
                                   @RequestParam String sortType,
                                   @RequestParam(required = false) String keyword,
                                   @RequestParam(required = false) String category,
                                   @UserForScale User user) {

        return itemService.getList(page, sortType, keyword, category, user);
    }
}