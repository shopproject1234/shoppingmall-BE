package com.sangwook.shoppingmall.entity.historyaggregate.domain;

import com.sangwook.shoppingmall.common.constant.Category;
import com.sangwook.shoppingmall.entity.cartaggregate.domain.Cart;
import com.sangwook.shoppingmall.entity.itemaggregate.domain.Item;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private Item item;

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDateTime date;

    private Integer count;

    private Integer totalPrice;

    public static History purchased(Cart cart) {
        History history = new History();
        history.user = cart.getUser();
        history.item = cart.getItem();
        history.category = cart.getCategory();
        history.count = cart.getCount();
        history.totalPrice = cart.getPrice() * cart.getCount();
        history.date = LocalDateTime.now();
        return history;
    }

    public History() {
    }
}
