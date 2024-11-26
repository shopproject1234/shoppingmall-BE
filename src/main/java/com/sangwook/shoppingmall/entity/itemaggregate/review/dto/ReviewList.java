package com.sangwook.shoppingmall.entity.itemaggregate.review.dto;

import com.sangwook.shoppingmall.entity.itemaggregate.domain.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewList {

    private Long reviewId;
    private Long userId;
    private String username;
    private String content;
    private Float score;
    private LocalDateTime time;

    public ReviewList(Review review) {
        this.reviewId = review.getId();
        this.userId = review.getUser().getId();
        this.username = review.getUser().getName();
        this.content = review.getContent();
        this.score = review.getScore();
        this.time = review.getTime();
    }

    public ReviewList() {
    }
}
