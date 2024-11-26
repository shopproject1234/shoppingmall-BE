package com.sangwook.shoppingmall.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sangwook.shoppingmall.application.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
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

    }

    @Test
    @DisplayName("사용자는 로그인할때 비밀번호가 다르면 로그인 할 수 없다")
    void test1_2() throws Exception {
        //TODO 추후 에러로직 처리 후 테스트 코드 작성
    }
}
