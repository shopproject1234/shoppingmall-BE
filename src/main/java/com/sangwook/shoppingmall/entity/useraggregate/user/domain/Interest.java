package com.sangwook.shoppingmall.entity.useraggregate.user.domain;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Preference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * AccessLevel을 Protected로 설정하여 무분별한 생성을 막음
 * Interest 객체는 User 안에서만 생성될 수 있다
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Preference scale;

    protected Interest(User user, Category category, Preference scale) {
        this.user = user;
        this.category = category;
        this.scale = scale;
    }

    protected Interest changeScale(Preference scale) {
        this.scale = scale;
        return this;
    }
}
