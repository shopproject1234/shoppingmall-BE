package com.sangwook.shoppingmall.entity.useraggregate.user.domain;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.dto.UserInfo;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.dto.UserRegister;
import com.sangwook.shoppingmall.exception.custom.ObjectNotFoundException;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;

import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

@Entity
@Getter
@Table(name = "Users", uniqueConstraints = @UniqueConstraint(columnNames = "email")) //이메일 제약 조건을 DB 수준에서 걸어주기
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Interest> interests = new ArrayList<>();

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
    public void addInterest(List<String> categories) {
        if (!categories.isEmpty()) {
            for (String category : categories) {
                interested(categoryConverter(category));
            }
        }
    }

    public void addInterest(Category category) {
        Optional<Interest> getInterest = getInterestWithCategory(category);
        if (getInterest.isEmpty()) {
            interested(category);
        } else { //이미 해당 카테고리의 관심사가 있는 경우 Preference를 INTERESTED로 변경
            getInterest.get().setInterested(true);
        }
    }

    private void deleteInterest(Category category) {
        Optional<Interest> interest = getInterestWithCategory(category);
        if (interest.isEmpty()) {
            throw new ObjectNotFoundException(getMethodName());
        }
        Interest get = interest.get();
        if (get.isInterested()) {
            get.setInterested(false);
        }
    }

    public User changeUserInfo(UserInfo userInfo) {
        name = userInfo.getNickname();
        if (userInfo.getCategory() == null) {
            deleteAllInterests();
            return this;
        }
        List<Category> change = userInfo.getCategory();

        List<Category> exist = new ArrayList<>();
        if (!interests.isEmpty()) {
            for (Interest getInterest : interests) {
                exist.add(getInterest.getCategory());
            }
        }

        //합집합
        Set<Category> set = new HashSet<>();
        set.addAll(change);
        set.addAll(exist);

        //차집합
        change.forEach(set::remove);

        for (Category category : change) {
            //있으면 scale변경, 없으면 저장
            addInterest(category);
        }

        for (Category category : set) {
            deleteInterest(category);
        }

        return this;
    }

    private void deleteAllInterests() {
        for (Interest interest : interests) {
            if (interest.isInterested()) {
                interest.setInterested(false);
            }
        }
    }

    private Category categoryConverter(String category) {
        return Category.valueOf(category);
    }

    private void interested(Category category) {
        Interest interest = new Interest(this, category, true);
        interests.add(interest);
    }

    /**
     * scale 가중치
     */
    public Interest plusScale(Category category, int scale) {
        Optional<Interest> getInterest = getInterestWithCategory(category);
        Interest interest;
        if (getInterest.isPresent()) {
            interest = getInterest.get();
            interest.plusScale(scale);
        } else {
            interest = new Interest(this, category);
            interest.plusScale(scale);
            interests.add(interest);
        }
        return interest;

    }

    /**
     * 이 메서드를 작동시키기 위해서는 fetch join 등 N+1 문제 방지가 필요하다.
     * 이 메서드를 사용하는(하나의 특정한 interest를 찾아야 하는) application 코드에서만 fetch join을 진행
     */
    private Optional<Interest> getInterestWithCategory(Category category) {
        for (Interest interest : interests) {
            if (interest.getCategory() == category) {
                return Optional.of(interest);
            }
        }
        return Optional.empty();
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
