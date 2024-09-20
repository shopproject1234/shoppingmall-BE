package com.sangwook.shoppingmall.domain.cart;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Cart {

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

    private Integer price; // 아이템 1개의 가격

    public static Cart add(User user, Item item, Integer count) {
        Cart cart = new Cart();
        cart.user = user;
        cart.item = item;
        cart.category = item.getCategory();
        cart.date = LocalDateTime.now();
        cart.count = count;
        cart.price = item.getPrice();
        return cart;
    }

    public void addCount(Integer count) {
        this.count += count;
    }

}
