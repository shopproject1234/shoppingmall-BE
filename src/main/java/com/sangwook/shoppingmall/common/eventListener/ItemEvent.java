package com.sangwook.shoppingmall.common.eventListener;

import com.sangwook.shoppingmall.entity.itemaggregate.domain.Item;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemEvent {

    private Item item;

    public ItemEvent(Item item) {
        this.item = item;
    }
}
