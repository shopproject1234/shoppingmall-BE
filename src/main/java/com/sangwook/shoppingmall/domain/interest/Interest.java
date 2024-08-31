package com.sangwook.shoppingmall.domain.interest;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Preference;
import com.sangwook.shoppingmall.domain.member.Member;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Preference scale;

    public Interest(Member member, Category category, Preference scale) {
        this.member = member;
        this.category = category;
        this.scale = scale;
    }

    public Interest() {
    }

    public Interest changeScale(Preference scale) {
        this.scale = scale;
        return this;
    }
}
