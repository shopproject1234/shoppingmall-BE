package com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto;

import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyItem {

    private Long itemId;
    private LocalDateTime time;
    private String itemName;
    private Integer itemCount;
    private Integer price; //1개당 가격

    public MyItem(Item item) {
        this.itemId = item.getId();
        this.time = item.getTime();
        this.itemName = item.getName();
        this.itemCount = item.getItemCount();
        this.price = item.getPrice();
    }

    public MyItem() {
    }
}
