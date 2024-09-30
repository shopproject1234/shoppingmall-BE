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

    public static Review write(User user, Item item, ReviewWrite reviewWrite) {
        Review review = new Review();
        review.user = user;
        review.item = item;
        review.content = reviewWrite.getContent();
        review.score = reviewWrite.getScore();
        review.time = LocalDateTime.now();
        return review;
    }

    public Review update(ReviewWrite reviewWrite) {
        this.content = reviewWrite.getContent();
        this.score = reviewWrite.getScore();
        this.time = LocalDateTime.now();
        return this;
    }

    public Review() {

    }
}
