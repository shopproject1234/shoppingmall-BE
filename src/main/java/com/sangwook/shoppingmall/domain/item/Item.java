package com.sangwook.shoppingmall.domain.item;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.review.Review;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String name;

    private Integer price;

    private Integer itemCount;

    private Integer reviewCount;

    private Float reviewAverage;

    private Integer sales;

    private Float score = 0f;

    @Column(length = 1000)
    private String itemInfo;

    public static Item add(AddItem addItem, User user) {
        Item item = new Item();
        item.category = addItem.getCategory();
        item.name = addItem.getItemName();
        item.price = addItem.getPrice();
        item.itemCount = addItem.getItemCount();
        item.user = user;
        item.itemInfo = addItem.getItemInfo();
        return item;
    }

    public void purchased(Integer count) {
        this.itemCount = this.itemCount - count;
        if (sales == null) {
            sales = count;
        } else {
            sales += count;
        }
    }

    public void plusReview(Review review) {
        if (this.reviewAverage == null) {
            this.reviewAverage = review.getScore();
        } else {
            this.reviewAverage = Math.round(((reviewAverage * reviewCount) + review.getScore()) / (reviewCount + 1) * 100) / 100.0f;
        }

        if (this.reviewCount == null) {
            this.reviewCount = 1;
        } else {
            this.reviewCount += 1;
        }
    }


}
