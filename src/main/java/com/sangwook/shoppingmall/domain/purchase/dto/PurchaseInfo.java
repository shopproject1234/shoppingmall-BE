package com.sangwook.shoppingmall.domain.purchase.dto;

import com.sangwook.shoppingmall.constant.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseInfo {

    private Long itemId;
    private String memberName;

    private Integer existItemCount;
    private Category category;
    private Integer price;
    private String itemName;

    public PurchaseInfo(Long itemId, String memberName, Integer existItemCount, Category category, Integer price, String itemName) {
        this.itemId = itemId;
        this.memberName = memberName;
        this.existItemCount = existItemCount;
        this.category = category;
        this.price = price;
        this.itemName = itemName;
    }
}
