package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.constant.Preference;
import com.sangwook.shoppingmall.domain.interest.Interest;
import com.sangwook.shoppingmall.domain.interest.dto.InterestInfo;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.user.dto.EmailCheck;
import com.sangwook.shoppingmall.domain.user.dto.UserRegister;
import com.sangwook.shoppingmall.service.fake.FakeEmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private FakeEmailService fakeEmailService;

    @Test
    @DisplayName("사용자는 회원가입 시 이메일 인증을 한다")
    void test1_1() {
        //given
        fakeEmailService.sendMail("tkddnr@naver.com");
        EmailCheck emailCheck = new EmailCheck();
        emailCheck.setEmail("tkddnr@naver.com");
        emailCheck.setCode(fakeEmailService.code);

        //when
        Boolean isChecked = fakeEmailService.verifyCode(emailCheck);

        //then
        assertThat(isChecked).isTrue();
        assertThat(fakeEmailService.emailAndCode.get("tkddnr@naver.com")).isEqualTo(fakeEmailService.code);

    }

    @Test
    @DisplayName("사용자는 회원가입을 할 수 있다")
    void test1_2() {
        //given
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", 20121122, 13,
                Gender.MALE);

        //when
        User saved = userService.register(userRegister);

        //then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("tkddnr@naver.com");
        assertThat(saved.getName()).isEqualTo("상욱");
        assertThat(passwordEncoder.matches("123123", saved.getPassword())).isTrue();
        assertThat(saved.getAge()).isEqualTo(13);
        assertThat(saved.getGender()).isEqualTo(Gender.MALE);
        assertThat(saved.getPhoneNumber()).isEqualTo("01011112222");
        assertThat(saved.getBirth()).isEqualTo(LocalDate.of(2012, 11, 22));

    }

    @Test
    @DisplayName("사용자는 로그인할 수 있다")
    void test2() {
        //TODO 로그인 부분 Session으로 관리하므로 LoginControllerTest로 옮길 예정

    }

    @Test
    @DisplayName("사용자는 로그아웃할 수 있다")
    void test3() {

    }

    @Test
    @DisplayName("사용자는 관심사를 정할 수 있다")
    void test4() {
        //given
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", 20121122, 13,
                Gender.MALE);
        User saved = userService.register(userRegister);

        //when
        Interest interest = userService.addInterest(saved.getId(), Category.FURNITURE);

        //then
        assertThat(interest.getUser().getId()).isEqualTo(saved.getId());
        assertThat(interest.getCategory()).isEqualTo(Category.FURNITURE);
        assertThat(interest.getScale()).isEqualTo(Preference.INTERESTED);
    }

    @Test
    @DisplayName("사용자는 관심사를 확인할 수 있다")
    void test5() {
        //given
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", 20121122, 13,
                Gender.MALE);
        User saved = userService.register(userRegister);

        //when
        userService.addInterest(saved.getId(), Category.FURNITURE);
        userService.addInterest(saved.getId(), Category.FABRIC);

        List<InterestInfo> interests = userService.getInterests(saved.getId());
        List<Category> categories = new ArrayList<>();

        categories.add(interests.get(0).getCategory());
        categories.add(interests.get(1).getCategory());

        //then
        assertThat(interests.size()).isEqualTo(2);
        assertThat(categories.contains(Category.FURNITURE)).isTrue();
        assertThat(categories.contains(Category.FABRIC)).isTrue();
    }

    @Test
    @DisplayName("사용자는 관심사를 삭제 할 수 있다")
    void test6() {
        //given
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", 20121122, 13,
                Gender.MALE);
        User saved = userService.register(userRegister);

        //when
        userService.addInterest(saved.getId(), Category.FURNITURE);
        userService.deleteInterest(saved.getId(), Category.FURNITURE);

        List<InterestInfo> interests = userService.getInterests(saved.getId());

        //then
        assertThat(interests.isEmpty()).isTrue();
    }

}