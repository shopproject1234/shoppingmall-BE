package com.sangwook.shoppingmall.domain.item;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Item {
    //TODO 추후 ITEM 추가한 Member가 누군지 알 수 있도록 추가 필요
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String name;

    private Integer price;

    private Integer itemCount;

    private Integer reviewCount;

    private Float reviewAverage;

    private Integer sales;

    private Float score = 0f;

    public static Item add(AddItem addItem) {
        Item item = new Item();
        item.category = addItem.getCategory();
        item.name = addItem.getName();
        item.price = addItem.getPrice();
        item.itemCount = addItem.getItemCount();
        return item;
    }


}
