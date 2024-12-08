package com.sangwook.shoppingmall.entity.useraggregate.user.domain;

import com.sangwook.shoppingmall.constant.Category;
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

    /**
     * 카테고리 10가지를 보유중인데 만약에 카테고리를 늘리라는 요청이 들어온다면?
     * 카테고리를 Enum으로 관리하는 것이 용이
     */
    @Enumerated(EnumType.STRING)
    private Category category;

    /**
     * 특정 카테고리에 대한 관심의 정도를 나타냄, 24/12/08 가중치로 변경
     * ex) 조회, 검색, 장바구니 추가, 구매할 경우 정도가 상승함
     */
    private int scale;

    //사용자가 관심사로 설정한 경우에만 true
    private boolean isInterested;

    protected Interest(User user, Category category) {
        this.user = user;
        this.category = category;
    }

    protected Interest(User user, Category category, boolean isInterested) {
        this.user = user;
        this.category = category;
        this.isInterested = isInterested;
    }

    protected Interest plusScale(int scale) {
        this.scale += scale;
        return this;
    }

    protected void setInterested(boolean isInterested) {
        this.isInterested = isInterested;
    }
}
