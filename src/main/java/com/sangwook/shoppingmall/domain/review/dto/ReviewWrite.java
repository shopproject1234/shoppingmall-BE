package com.sangwook.shoppingmall.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewWrite {
    private String title;
    private String content;
    private Float point;
}
