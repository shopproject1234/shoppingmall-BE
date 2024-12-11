package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.common.constant.Category;
import com.sangwook.shoppingmall.common.constant.Gender;
import com.sangwook.shoppingmall.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.interest.infra.InterestRepository;
import com.sangwook.shoppingmall.entity.useraggregate.domain.Interest;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.dto.UserInfo;
import com.sangwook.shoppingmall.entity.useraggregate.user.dto.UserRegister;
import com.sangwook.shoppingmall.entity.useraggregate.user.infra.UserRepository;
import com.sangwook.shoppingmall.service.fake.FakeEmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private UserRepository userRepository;

    User user;

    @BeforeEach
    void setUser() {
        UserRegister userRegister = new UserRegister();
        userRegister.setBirth("2000-09-14");
        userRegister.setEmail("sangwook@naver.com");
        userRegister.setPassword("123123");
        userRegister.setNickname("sangwook");
        userRegister.setGender(Gender.MALE);
        userRegister.setPhoneNumber("01011111111");
        userRegister.setCategory(new ArrayList<>());
        user = userService.register(userRegister);
    }

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
        //given
        UserInfo userInfo = new UserInfo();
        userInfo.setCategory(List.of(Category.APPLIANCE, Category.KITCHENWARE));
        userInfo.setNickname("sangwook1");

        //when
        userService.changeUserInfo(user.getId(), userInfo);

        //then
        User getUser = userRepository.findById(user.getId()).get();
        assertThat(getUser.getInterests()).size().isEqualTo(2);
        List<Category> categoryList = new ArrayList<>();
        List<Interest> interests = getUser.getInterests();
        for (Interest interest : interests) {
            if (interest.isInterested()) {
                categoryList.add(interest.getCategory());
            }
        }

        assertThat(categoryList.size()).isEqualTo(2);
        assertThat(categoryList.contains(Category.APPLIANCE)).isTrue();
        assertThat(categoryList.contains(Category.KITCHENWARE)).isTrue();
        assertThat(getUser.getName()).isEqualTo("sangwook1");

    }

    @Test
    @DisplayName("사용자는 관심사를 삭제할 수 있다")
    void test5_2() {
        //given
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setCategory(List.of(Category.APPLIANCE, Category.KITCHENWARE));
        userInfo1.setNickname("sangwook1");

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setCategory(new ArrayList<>());
        userInfo2.setNickname("sangwook1");

        //when
        userService.changeUserInfo(user.getId(), userInfo1);
        userService.changeUserInfo(user.getId(), userInfo2);

        //then
        User getUser = userRepository.findById(user.getId()).get();

        List<Interest> interests = getUser.getInterests();
        for (Interest interest : interests) {
            assertThat(interest.isInterested()).isFalse();
        }

    }

    @Test
    @DisplayName("사용자는 관심사를 추가하면서 삭제할 수 있다")
    void test5_3() {
        //given
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setCategory(List.of(Category.APPLIANCE, Category.KITCHENWARE));
        userInfo1.setNickname("sangwook1");

        UserInfo userInfo2 = new UserInfo();
        userInfo2.setCategory(List.of(Category.FURNITURE, Category.KITCHENWARE)); //FURNITURE 추가, KITCHENWARE 유지, APPLIANCE 삭제
        userInfo2.setNickname("sangwook1");

        //when
        userService.changeUserInfo(user.getId(), userInfo1);
        userService.changeUserInfo(user.getId(), userInfo2);

        //then
        User getUser = userRepository.findById(user.getId()).get();

        List<Category> categoryList = new ArrayList<>();
        List<Interest> interests = getUser.getInterests();
        for (Interest interest : interests) {
            if (interest.isInterested()) {
                categoryList.add(interest.getCategory());
            }
        }

        assertThat(categoryList.size()).isEqualTo(2);
        assertThat(categoryList.contains(Category.FURNITURE)).isTrue();
        assertThat(categoryList.contains(Category.KITCHENWARE)).isTrue();
        assertThat(categoryList.contains(Category.APPLIANCE)).isFalse();
        assertThat(getUser.getName()).isEqualTo("sangwook1");
    }

    @Test
    @DisplayName("사용자는 비밀번호를 변경할 수 있다")
    void test6() {

    }
}