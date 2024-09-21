package com.sangwook.shoppingmall.controller;

import com.sangwook.shoppingmall.argumentResolver.Login;
import com.sangwook.shoppingmall.constant.SessionConst;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.user.dto.EmailCheck;
import com.sangwook.shoppingmall.domain.user.dto.PassCheck;
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
        if (userService.checkUserExist(check.getEmail())) {
            //해당 유저가 이미 존재하는 경우
            throw new IllegalStateException(); //FIXME
        }
        emailService.sendMail(check.getEmail());
    }

    @PostMapping("/user/pwCheck")
    public void sendEmail(@RequestBody PassCheck check, @Login User user) {
        Boolean checked = userService.checkPass(check, user);
        if (checked) {
            emailService.sendMail(user.getEmail());
        }
    }

    @PostMapping("/user/email/verify")
    public Boolean verifyCode(@RequestBody EmailCheck check) { //계정 생성 시, 비밀번호 변경 시 이메일체크를 담당
        return emailService.verifyCode(check);
    }
}
