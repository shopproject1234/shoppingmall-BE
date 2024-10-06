package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.constant.Preference;
import com.sangwook.shoppingmall.domain.interest.Interest;
import com.sangwook.shoppingmall.domain.interest.dto.InterestInfo;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.user.dto.EmailCheck;
import com.sangwook.shoppingmall.domain.user.dto.UserInfo;
import com.sangwook.shoppingmall.domain.user.dto.UserRegister;
import com.sangwook.shoppingmall.exception.custom.EmailSendException;
import com.sangwook.shoppingmall.repository.InterestRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
                "01011112222", "2012-11-22",
                Gender.MALE);

        //when
        User saved = userService.register(userRegister);

        //then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo("tkddnr@naver.com");
        assertThat(saved.getName()).isEqualTo("상욱");
        assertThat(passwordEncoder.matches("123123", saved.getPassword())).isTrue();
        assertThat(saved.getGender()).isEqualTo(Gender.MALE);
        assertThat(saved.getPhoneNumber()).isEqualTo("01011112222");
        assertThat(saved.getBirth()).isEqualTo(LocalDate.of(2012, 11, 22));

    }

    @Test
    @DisplayName("사용자는 회원가입을 할 때 관심사를 지정할 수 있다")
    void test1_3() {
        //given
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE,  List.of("FURNITURE", "FABRIC"));

        //when
        User saved = userService.register(userRegister);

        //then
        List<Interest> interests = interestRepository.findAllById(saved.getId());
        assertThat(interests.size()).isEqualTo(2);

        List<Category> categories = new ArrayList<>();
        for (Interest interest : interests) {
            categories.add(interest.getCategory());
        }

        assertThat(categories.contains(Category.FABRIC)).isTrue();
        assertThat(categories.contains(Category.FURNITURE)).isTrue();
    }

    @Test
    @DisplayName("이메일 발송 MessagingException 테스트 : 커스텀 런타임 예외인 EmailSendException으로 변환")
    void test1_4() {
        assertThatThrownBy(() -> fakeEmailService.exceptionTest()).isInstanceOf(EmailSendException.class);
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
        //given
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE,  List.of("FURNITURE", "FABRIC"));
        User saved = userService.register(userRegister);

        //when
        UserInfo userInfo = userService.getUserInfo(saved.getId());

        //then
        List<Category> category = userInfo.getCategory();
        assertThat(category.size()).isEqualTo(2);
        assertThat(category.contains(Category.FURNITURE)).isTrue();
        assertThat(category.contains(Category.FABRIC)).isTrue();
        assertThat(userInfo.getNickname()).isEqualTo("상욱");

    }

    @Test
    @DisplayName("사용자는 관심사를 추가할 수 있다")
    void test5_1() {
        //given
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE);
        User saved = userService.register(userRegister);

        UserInfo userInfo = new UserInfo();
        userInfo.setCategory(List.of(Category.FURNITURE, Category.APPLIANCE));
        userInfo.setNickname("상욱");
        userService.changeUserInfo(saved.getId(), userInfo);

        //when
        userInfo = userService.getUserInfo(saved.getId());

        //then
        List<Category> category = userInfo.getCategory();
        assertThat(category.size()).isEqualTo(2);
        assertThat(category.contains(Category.FURNITURE)).isTrue();
        assertThat(category.contains(Category.APPLIANCE)).isTrue();
        assertThat(userInfo.getNickname()).isEqualTo("상욱");
    }

    @Test
    @DisplayName("사용자는 관심사를 삭제할 수 있다")
    void test5_2() {
        //given
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE,  List.of("FURNITURE", "FABRIC"));
        User saved = userService.register(userRegister);

        UserInfo userInfo = new UserInfo();
        userInfo.setNickname("상욱");
        userService.changeUserInfo(saved.getId(), userInfo);

        //when
        userInfo = userService.getUserInfo(saved.getId());

        //then
        List<Category> category = userInfo.getCategory();
        assertThat(category).isEmpty();
        assertThat(userInfo.getNickname()).isEqualTo("상욱");
    }

    @Test
    @DisplayName("사용자는 관심사를 추가하면서 삭제할 수 있다")
    void test5_3() {
        //given
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE,  List.of("FURNITURE", "FABRIC"));
        User saved = userService.register(userRegister);

        UserInfo userInfo = new UserInfo();
        userInfo.setCategory(List.of(Category.FURNITURE, Category.APPLIANCE));
        userInfo.setNickname("상욱");
        userService.changeUserInfo(saved.getId(), userInfo);

        //when
        userInfo = userService.getUserInfo(saved.getId());

        //then
        List<Category> category = userInfo.getCategory();
        assertThat(category.size()).isEqualTo(2);
        assertThat(category.contains(Category.FURNITURE)).isTrue();
        assertThat(category.contains(Category.APPLIANCE)).isTrue();
        assertThat(userInfo.getNickname()).isEqualTo("상욱");
    }

    @Test
    @DisplayName("사용자는 비밀번호를 변경할 수 있다")
    void test6() {

    }
}