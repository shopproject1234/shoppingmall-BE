package com.sangwook.shoppingmall.domain.item;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String name;

    private Integer price;

    private Integer itemCount;

    private Integer reviewCount;

    private Float reviewAverage;

    private Integer sales;

    private Float score = 0f;

    public static Item add(AddItem addItem, Member member) {
        Item item = new Item();
        item.category = addItem.getCategory();
        item.name = addItem.getName();
        item.price = addItem.getPrice();
        item.itemCount = addItem.getItemCount();
        item.member = member;
        return item;
    }

    public void minusCount(Integer count) {
        this.itemCount = this.itemCount - count;
    }


}
