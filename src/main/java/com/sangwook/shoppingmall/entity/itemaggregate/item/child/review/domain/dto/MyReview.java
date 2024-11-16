package com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MyReview {

    private Long itemId;
    private LocalDateTime time;
    private String content;
    private Integer score;

    public MyReview(Long itemId, LocalDateTime time, String content, Integer score) {
        this.itemId = itemId;
        this.time = time;
        this.content = content;
        this.score = score;
    }

    public MyReview() {
    }
}
