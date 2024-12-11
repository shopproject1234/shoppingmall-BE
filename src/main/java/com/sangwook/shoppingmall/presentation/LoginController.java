package com.sangwook.shoppingmall.presentation;

import com.sangwook.shoppingmall.common.argumentResolver.Login;
import com.sangwook.shoppingmall.common.constant.SessionConst;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.dto.*;
import com.sangwook.shoppingmall.exception.custom.UserValidationException;
import com.sangwook.shoppingmall.application.EmailService;
import com.sangwook.shoppingmall.application.RedisService;
import com.sangwook.shoppingmall.application.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class LoginController {

    private final UserService userService;
    private final EmailService emailService;
    private final RedisService redisService;

    /**
     * 만약 악의적인 사용자가 이메일 인증을 완료해서 code를 1로 변환시켜놓고 동시에 회원가입을 누른다면? -> 2개의 회원이 생김
     * email을 unique 제약 조건을 걸어 해결
     */
    @PostMapping("/register")
    public void register(@RequestBody UserRegister userRegister) {
        //사용자가 이메일 확인을 받지않았다면(code != 1일 경우) 돌려보내기
        if (!redisService.getCode(userRegister.getEmail()).equals(1)) {
            throw new UserValidationException("이메일이 인증되지 않았습니다", getMethodName());
        }
        userService.register(userRegister);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody UserLogin userLogin, HttpServletRequest request) {
        User user = userService.login(userLogin);
        request.getSession().setAttribute(SessionConst.LOGIN_USER, user);
        return new LoginResponse(user.getId(), user.getName());
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
    }

    @PostMapping("/email/auth")
    public void sendEmail(@RequestBody EmailCheck check) {
        if (userService.checkUserExist(check.getEmail())) {
            //해당 유저가 이미 존재하는 경우
            throw new UserValidationException("해당 유저는 이미 존재합니다", getMethodName());
        }
        emailService.sendVerifyMail(check.getEmail());
    }

    @PostMapping("/pwCheck")
    public void sendEmail(@RequestBody PassCheck check, @Login User user) {
        Boolean checked = userService.checkPass(check, user);
        if (checked) {
            emailService.sendVerifyMail(user.getEmail());
        }
    }

    @PostMapping("/email/verify")
    public Boolean verifyCode(@RequestBody EmailCheck check) { //계정 생성 시, 비밀번호 변경 시 이메일체크를 담당
        return emailService.verifyCode(check);
    }

    @PostMapping("/email/valid")
    public ResponseEntity<Result> validEmail(@RequestBody EmailValid emailValid) {
        Result result = new Result();
        result.setResult(userService.validEmail(emailValid));
        return ResponseEntity.ok(result);
    }
}