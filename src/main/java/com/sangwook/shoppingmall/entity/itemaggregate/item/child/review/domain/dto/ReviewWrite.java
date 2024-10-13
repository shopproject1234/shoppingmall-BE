package com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewWrite { // 리뷰 생성, 수정에 사용
    private String content;
    private Float score;
}
