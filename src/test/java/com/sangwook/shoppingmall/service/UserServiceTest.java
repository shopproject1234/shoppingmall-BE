package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.user.dto.EmailCheck;
import com.sangwook.shoppingmall.domain.user.dto.UserRegister;
import com.sangwook.shoppingmall.service.fake.FakeEmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
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
        Boolean isChecked = fakeEmailService.checkCode(emailCheck);

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
                "01011112222", LocalDate.of(2012, 11, 22), 13,
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

    }

    @Test
    @DisplayName("사용자는 로그아웃할 수 있다")
    void test3() {

    }

    @Test
    @DisplayName("사용자는 관심사를 정할 수 있다")
    void test4() {

    }

    @Test
    @DisplayName("사용자는 관심사를 확인할 수 있다")
    void test5() {

    }

}