package com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.ItemImage;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ItemInfo {

    private Long itemId;
    private String itemName;
    private Integer price;
    private Category category;
    private Integer itemCount;
    private String itemInfo;
    private LocalDateTime uploadTime;

    private Long uploadUserId;
    private String uploadUserName;

    private List<String> image;

    private Integer reviewCount;
    private Float reviewAverage;
    private Integer sales;

    public ItemInfo(Item item, User user, List<ItemImage> image) {
        this.itemId = item.getId();
        this.itemName = item.getName();
        this.price = item.getPrice();
        this.category = item.getCategory();
        this.itemCount = item.getItemCount();
        this.itemInfo = item.getItemInfo();
        this.uploadTime = item.getTime();

        this.uploadUserId = user.getId();
        this.uploadUserName = user.getName();

        this.image = image.stream().map(ItemImage::getImageLink).toList();

        this.reviewCount = item.getReviewCount();
        this.reviewAverage = item.getReviewAverage();
        this.sales = item.getSales();
    }
}