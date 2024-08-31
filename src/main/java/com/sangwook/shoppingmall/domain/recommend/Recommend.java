package com.sangwook.shoppingmall.domain.recommend;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Preference;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Recommend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "memberId")
//    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private Item item;

    private Preference memberPreference;

    private Float itemScore;

    private Float totalScore;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String name;

    private Integer price;

    private Integer itemCount;

    private Integer reviewCount;

    private Float reviewAverage;

    private Integer sales;


    public Recommend(Item item) {
        this.item = item;
        this.category = item.getCategory();
        this.name = item.getName();
        this.price = item.getPrice();
        this.itemCount = item.getItemCount();
        this.reviewCount = item.getReviewCount();
        this.reviewAverage = item.getReviewAverage();
        this.sales = item.getSales();
    }
}
