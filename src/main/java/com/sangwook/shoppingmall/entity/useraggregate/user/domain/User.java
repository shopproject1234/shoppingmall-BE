package com.sangwook.shoppingmall.entity.useraggregate.user.domain;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.constant.Preference;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.dto.UserRegister;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String phoneNumber;

    private LocalDate birth;

    @OneToMany(mappedBy = "user")
    private List<Interest> interests;

    public static User register(UserRegister userRegister, String encoded) {
        User user = new User();
        user.email = userRegister.getEmail();
        user.name = userRegister.getNickname();
        user.password = encoded;
        user.gender = userRegister.getGender();
        user.phoneNumber = userRegister.getPhoneNumber();
        user.birth = birthConverter(userRegister.getBirth());
        return user;
    }

    public User() {

    }

    private static LocalDate birthConverter(String birth) {
        int year = Integer.parseInt(birth.substring(0, 4));
        int month = Integer.parseInt(birth.substring(5, 7));
        int day = Integer.parseInt(birth.substring(8));

        return LocalDate.of(year, month, day);
    }

    /**
     * Interest
     */
    public Interest interested(Category category) {
        Interest interest = new Interest(this, category, Preference.INTERESTED);
        interests.add(interest);
        return interest;
    }

    public Interest changeScale(Category category, Preference scale) {
        Interest interest = getInterestWithCategory(category);
        return interest.changeScale(scale);
    }

    /**
     * 이 메서드를 작동시키기 위해서는 fetch join 등 N+1 문제 방지가 필요하다.
     * 이 메서드를 사용하는(하나의 특정한 interest를 찾아야 하는) application 코드에서만 fetch join을 진행
     */
    private Interest getInterestWithCategory(Category category) {
        for (Interest interest : interests) {
            if (interest.getCategory() == category) {
                return interest;
            }
        }
        throw new IllegalStateException(); //FIXME
    }

    //이메일이 같으면 같은 유저라고 판단
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(email);
    }
}
