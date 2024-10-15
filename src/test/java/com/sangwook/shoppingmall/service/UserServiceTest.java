package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.entity.useraggregate.user.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.user.child.interest.infra.InterestRepository;
import com.sangwook.shoppingmall.service.fake.FakeEmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private InterestRepository interestRepository;

    @Test
    @DisplayName("사용자는 회원가입 시 이메일 인증을 한다")
    void test1_1() {

    }

    @Test
    @DisplayName("사용자는 회원가입을 할 수 있다")
    void test1_2() {

    }

    @Test
    @DisplayName("사용자는 회원가입을 할 때 관심사를 지정할 수 있다")
    void test1_3() {
    }

    @Test
    @DisplayName("이메일 발송 MessagingException 테스트 : 커스텀 런타임 예외인 EmailSendException으로 변환")
    void test1_4() {

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

    /**
     * 사용자 정보
     */
    @Test
    @DisplayName("사용자는 관심사를 확인할 수 있다")
    void test4() {

    }

    @Test
    @DisplayName("사용자는 관심사를 추가할 수 있다")
    void test5_1() {
    }

    @Test
    @DisplayName("사용자는 관심사를 삭제할 수 있다")
    void test5_2() {
    }

    @Test
    @DisplayName("사용자는 관심사를 추가하면서 삭제할 수 있다")
    void test5_3() {
    }

    @Test
    @DisplayName("사용자는 비밀번호를 변경할 수 있다")
    void test6() {

    }
}