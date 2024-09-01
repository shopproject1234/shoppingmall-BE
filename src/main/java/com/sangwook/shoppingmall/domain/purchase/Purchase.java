package com.sangwook.shoppingmall.domain.purchase;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private Item item;

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDateTime date;

    private Integer count;

    private Integer totalPrice;

    public Purchase(Member member, Item item, Category category, Integer count, Integer totalPrice) {
        this.member = member;
        this.item = item;
        this.category = category;
        this.count = count;
        this.totalPrice = totalPrice;
        this.date = LocalDateTime.now();
    }

    public Purchase() {
    }
}
