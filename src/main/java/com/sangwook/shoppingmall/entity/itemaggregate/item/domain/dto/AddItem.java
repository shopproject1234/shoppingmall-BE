package com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto;

import com.sangwook.shoppingmall.constant.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AddItem {
    private String itemName;
    private Integer itemCount;
    private String itemInfo;
    private Category category;
    private Integer price;

    private List<String> image;
}
