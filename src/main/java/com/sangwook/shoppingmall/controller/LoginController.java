package com.sangwook.shoppingmall.controller;

import com.sangwook.shoppingmall.constant.SessionConst;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.user.dto.EmailCheck;
import com.sangwook.shoppingmall.domain.user.dto.UserLogin;
import com.sangwook.shoppingmall.domain.user.dto.UserRegister;
import com.sangwook.shoppingmall.service.EmailService;
import com.sangwook.shoppingmall.service.RedisService;
import com.sangwook.shoppingmall.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final EmailService emailService;
    private final RedisService redisService;

    @PostMapping("/user/register")
    public void register(@RequestBody UserRegister userRegister) {
        //사용자가 이메일 확인을 받지않았다면(code != 1일 경우) 돌려보내기
        if (!redisService.getCode(userRegister.getEmail()).equals(1)) {
            throw new IllegalStateException();//FIXME
        }
        userService.register(userRegister);
    }

    @PostMapping("/user/login")
    public String login(@RequestBody UserLogin userLogin, HttpServletRequest request) {
        User user = userService.login(userLogin);
        request.getSession().setAttribute(SessionConst.LOGIN_USER, user);
        return "ok";
    }

    @PostMapping("/user/email/auth")
    public void sendEmail(@RequestBody EmailCheck check) {
        emailService.sendMail(check.getEmail());
    }

    @PostMapping("/user/email/verify")
    public Boolean verifyCode(@RequestBody EmailCheck check) {
        return emailService.verifyCode(check);
    }
}
