package com.sangwook.shoppingmall.entity.historyaggregate.history.domain.dto;

import com.sangwook.shoppingmall.entity.historyaggregate.history.domain.History;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyPurchase {

    private Long itemId;
    private LocalDateTime time;
    private String itemName;
    private Integer itemCount;
    private Integer price;
    private Integer totalPrice;

    public MyPurchase(History history) {
        this.itemId = history.getItem().getId();
        this.time = history.getDate();
        this.itemName = history.getItem().getName();
        this.itemCount = history.getCount();
        this.price = history.getItem().getPrice();
        this.totalPrice = history.getTotalPrice();
    }

    public MyPurchase() {
    }
}
