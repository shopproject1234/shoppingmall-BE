package com.sangwook.shoppingmall.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewPage {

    private Long reviewId;
    private Long userId;
    private String username;
    private String content;
    private Float score;
    private LocalDateTime time;

}
