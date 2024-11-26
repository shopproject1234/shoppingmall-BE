package com.sangwook.shoppingmall.entity.itemaggregate.review.dto;

import com.sangwook.shoppingmall.entity.itemaggregate.domain.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyReview {

    private Long itemId;
    private LocalDateTime time;
    private String content;
    private Float score;

    public MyReview(Review review) {
        this.itemId = review.getItem().getId();
        this.time = review.getTime();
        this.content = review.getContent();
        this.score = review.getScore();
    }

    public MyReview() {
    }
}
