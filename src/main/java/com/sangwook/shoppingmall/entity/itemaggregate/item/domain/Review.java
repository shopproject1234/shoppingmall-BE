package com.sangwook.shoppingmall.entity.itemaggregate.item.domain;

import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.exception.custom.UserValidationException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private float score;

    private LocalDateTime time;

    protected Review(User user, Item item, String content, float score) {
        this.user = user;
        this.item = item;
        this.content = content;
        this.time = LocalDateTime.now();
        this.score = score;
    }

    protected Review update(String content, float score) {
        this.content = content;
        this.score = score;
        return this;
    }

    //id가 같을 경우 같은 리뷰로 판단
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Review review = (Review) object;
        return Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
