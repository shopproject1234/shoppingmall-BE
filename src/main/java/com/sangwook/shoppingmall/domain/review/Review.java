package com.sangwook.shoppingmall.domain.review;

import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.member.Member;
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
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemId")
    private Item item;

    private String title;

    private String content;

    private Float point;

    private LocalDateTime time;

    public Review(Member member, Item item, ReviewWrite reviewWrite) {
        this.member = member;
        this.item = item;
        this.title = reviewWrite.getTitle();
        this.content = reviewWrite.getContent();
        this.point = reviewWrite.getPoint();
        this.time = LocalDateTime.now();
    }

    public Review() {

    }
}
