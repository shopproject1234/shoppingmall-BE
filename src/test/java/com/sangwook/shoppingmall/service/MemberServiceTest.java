package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.constant.Preference;
import com.sangwook.shoppingmall.domain.member.Member;
import com.sangwook.shoppingmall.domain.member.dto.MemberLogin;
import com.sangwook.shoppingmall.domain.member.dto.MemberRegister;
import com.sangwook.shoppingmall.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("사용자는 회원가입을 할 수 있다")
    void test1() {
        //given
        MemberRegister memberRegister = new MemberRegister();
        memberRegister.setEmail("tkddnr@naver.com");
        memberRegister.setPassword("123123");
        memberRegister.setAge(13);
        memberRegister.setName("상욱");
        memberRegister.setGender(Gender.MALE);

        //when
        memberService.register(memberRegister);

        //then
        Member getMember = memberRepository.findByEmail("tkddnr@naver.com").get();

        assertThat(getMember.getEmail()).isEqualTo("tkddnr@naver.com");
        assertThat(passwordEncoder.matches(memberRegister.getPassword(), getMember.getPassword())).isTrue();
        assertThat(getMember.getAge()).isEqualTo(13);
        assertThat(getMember.getName()).isEqualTo("상욱");
        assertThat(getMember.getGender()).isEqualTo(Gender.MALE);

    }

    @Test
    @DisplayName("사용자는 회원가입을 할 때 관심사를 정할 수 있다")
    void test2() {
        //given
        MemberRegister memberRegister = new MemberRegister();
        memberRegister.setEmail("tkddnr@naver.com");
        memberRegister.setPassword("123123");
        memberRegister.setAge(13);
        memberRegister.setName("상욱");
        memberRegister.setGender(Gender.MALE);
        memberRegister.setFurniture(true);

        //when
        memberService.register(memberRegister);

        //then
        Member getMember = memberRepository.findByEmail("tkddnr@naver.com").get();
        assertThat(getMember.getFurniture()).isEqualTo(Preference.INTERESTED);
    }

    @Test
    @DisplayName("사용자는 로그인할 수 있다")
    void test3() {
        //given
        MemberRegister memberRegister = new MemberRegister();
        memberRegister.setEmail("tkddnr@naver.com");
        memberRegister.setPassword("123123");
        memberRegister.setAge(13);
        memberRegister.setName("상욱");
        memberRegister.setGender(Gender.MALE);
        memberRegister.setFurniture(true);

        //when
        memberService.register(memberRegister);
        MemberLogin memberLogin = new MemberLogin();
        memberLogin.setEmail("tkddnr@naver.com");
        memberLogin.setPassword("123123");

        Member member = memberService.login(memberLogin);

        assertThat(member.getEmail()).isEqualTo("tkddnr@naver.com");
        assertThat(passwordEncoder.matches(memberLogin.getPassword(), member.getPassword())).isTrue();
        assertThat(member.getAge()).isEqualTo(13);
        assertThat(member.getName()).isEqualTo("상욱");
        assertThat(member.getGender()).isEqualTo(Gender.MALE);
    }

}