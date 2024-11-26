package com.sangwook.shoppingmall.entity.itemaggregate.item.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sangwook.shoppingmall.common.constant.Category;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ItemList implements Serializable {

    private Long itemId;
    private String itemName;
    private Integer price;
    private Category category;
    private Integer itemCount;
    private LocalDateTime uploadTime;

    private Long uploadUserId;
    private String uploadUserName;
    private String titleImage;

    @QueryProjection
    public ItemList(Long itemId, String itemName, Integer price, Category category, Integer itemCount, LocalDateTime uploadTime, Long uploadUserId, String uploadUserName, String titleImage) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.category = category;
        this.itemCount = itemCount;
        this.uploadTime = uploadTime;
        this.uploadUserId = uploadUserId;
        this.uploadUserName = uploadUserName;
        this.titleImage = titleImage;
    }
}
