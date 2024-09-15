package com.sangwook.shoppingmall.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.constant.SessionConst;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.user.dto.UserLogin;
import com.sangwook.shoppingmall.domain.user.dto.UserRegister;
import com.sangwook.shoppingmall.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class LoginControllerTest {

    @Autowired
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("사용자는 로그인할 수 있다")
    void test1_1() throws Exception {
        //given
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", 20121122, 13,
                Gender.MALE);
        User saved = userService.register(userRegister);

        //when
        UserLogin userLogin = new UserLogin();
        userLogin.setEmail("tkddnr@naver.com");
        userLogin.setPassword("123123");

        //then
        mvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLogin)))
                .andExpectAll(
                        MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().string("ok"),
                        MockMvcResultMatchers.request().sessionAttribute(SessionConst.LOGIN_USER, saved) //파고파고 들어가보면 equals 비교를한다 User안에 equals를 email비교로 등록해놓았다
                );
    }

    @Test
    @DisplayName("사용자는 로그인할때 비밀번호가 다르면 로그인 할 수 없다")
    void test1_2() throws Exception {
        //TODO 추후 에러로직 처리 후 테스트 코드 작성
    }
}
