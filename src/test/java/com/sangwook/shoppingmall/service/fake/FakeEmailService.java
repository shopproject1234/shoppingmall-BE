package com.sangwook.shoppingmall.service.fake;

import com.sangwook.shoppingmall.domain.user.dto.EmailCheck;
import com.sangwook.shoppingmall.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FakeEmailService implements EmailService {

    public int code;
    public Map<String, Integer> emailAndCode = new HashMap<>();

    @Override
    public void sendMail(String email) {
        code = generateCode();
        emailAndCode.put(email, code);
    }

    @Override
    public Boolean verifyCode(EmailCheck check) {
        return emailAndCode.get(check.getEmail()).equals(check.getCode());
    }

    private int generateCode() {
        return (int)(Math.random() * (90000)) + 100000;
    }
}
