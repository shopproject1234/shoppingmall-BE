package com.sangwook.shoppingmall.domain.item.dto;

import com.sangwook.shoppingmall.constant.Category;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddItem {
    private Category category;
    private String name;
    private Integer price;
    private Integer itemCount;
}
