package com.sangwook.shoppingmall.domain.item.dto;

import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemInfo {

    private String memberName;

    private String category;

    private String name;

    private Integer price;

    private Integer itemCount;

    private Integer reviewCount;

    private Float reviewAverage;

    private Integer sales;

    public ItemInfo(Item item, Member member) {
        this.memberName = member.getName();
        this.category = item.getCategory().displayName;
        this.name = item.getName();
        this.price = item.getPrice();
        this.itemCount = item.getItemCount();
        this.reviewCount = item.getReviewCount();
        this.reviewAverage = item.getReviewAverage();
        this.sales = item.getSales();
    }
}
