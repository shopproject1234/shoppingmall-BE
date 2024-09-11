package com.sangwook.shoppingmall.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("사용자는 회원가입 시 이메일 인증을 한다")
    void test1_1() {

    }

    @Test
    @DisplayName("사용자는 회원가입을 할 수 있다")
    void test1_2() {

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