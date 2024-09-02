package com.sangwook.shoppingmall.domain.review.dto;

import com.sangwook.shoppingmall.domain.review.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewPage {

    private String writer;
    private String title;
    private String content;
    private Float point;
    private LocalDateTime time;

    public ReviewPage(String writer, String title, String content, Float point, LocalDateTime time) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.point = point;
        this.time = time;
    }
}
