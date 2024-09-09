package com.sangwook.shoppingmall.domain.history;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.user.User;
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

    private Integer price;

    public History(User user, Item item, Category category, Integer count, Integer price) {
        this.user = user;
        this.item = item;
        this.category = category;
        this.count = count;
        this.price = price;
        this.date = LocalDateTime.now();
    }

    public History() {
    }
}
