package com.sangwook.shoppingmall.domain.review;

import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.review.dto.ReviewWrite;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
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

    private Float score;

    private LocalDateTime time;

    public Review(User user, Item item, ReviewWrite reviewWrite) {
        this.user = user;
        this.item = item;
        this.content = reviewWrite.getContent();
        this.score = reviewWrite.getScore();
        this.time = LocalDateTime.now();
    }

    public Review() {

    }
}
