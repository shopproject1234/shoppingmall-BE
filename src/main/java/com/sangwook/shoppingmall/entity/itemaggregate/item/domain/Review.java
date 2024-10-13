package com.sangwook.shoppingmall.entity.itemaggregate.item.domain;

import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private Item item;

    private String content;

    private float score;

    private LocalDateTime time;

    protected Review(User user, Item item, String content, float score) {
        this.user = user;
        this.item = item;
        this.content = content;
        this.time = LocalDateTime.now();
        this.score = score;
    }

    protected Review update(String content, float score) {
        this.content = content;
        this.score = score;
        return this;
    }
}
