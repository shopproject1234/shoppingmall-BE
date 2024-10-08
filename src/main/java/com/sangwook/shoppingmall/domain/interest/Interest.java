package com.sangwook.shoppingmall.domain.interest;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Preference;
import com.sangwook.shoppingmall.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
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

    public static Interest interested(User user, Category category) {
        Interest interest = new Interest();
        interest.user = user;
        interest.category = category;
        interest.scale = Preference.INTERESTED;
        return interest;
    }

    public Interest() {
    }

    public Interest changeScale(Preference scale) {
        this.scale = scale;
        return this;
    }
}
