package com.sangwook.shoppingmall.controller;

import com.sangwook.shoppingmall.domain.user.dto.EmailCheck;
import com.sangwook.shoppingmall.domain.user.dto.UserRegister;
import com.sangwook.shoppingmall.service.EmailService;
import com.sangwook.shoppingmall.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/user/register")
    public void register(@RequestBody UserRegister userRegister) {

    }

    @PostMapping("/user/email")
    public void sendEmail(@RequestBody EmailCheck check) {
        emailService.sendCode(check.getEmail());
    }

    @GetMapping("/user/email")
    public void checkCode(@RequestBody EmailCheck check) {
        Boolean isChecked = emailService.checkCode(check);
        log.info("isChecked: {}", isChecked);
    }
}
