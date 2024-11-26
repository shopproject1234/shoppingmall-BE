package com.sangwook.shoppingmall.service.fake;

import com.sangwook.shoppingmall.entity.itemaggregate.domain.Item;
import com.sangwook.shoppingmall.entity.useraggregate.user.dto.EmailCheck;
import com.sangwook.shoppingmall.exception.custom.EmailSendException;
import com.sangwook.shoppingmall.application.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

@Service
public class FakeEmailService implements EmailService {

    public int code;
    public Map<String, Integer> emailAndCode = new HashMap<>();
    public boolean sended;

    @Override
    public void sendVerifyMail(String email) {
        code = generateCode();
        emailAndCode.put(email, code);
    }

    @Override
    public void sendItemMail(Item item) {
        sended = true;
    }

    @Override
    public Boolean verifyCode(EmailCheck check) {
        return emailAndCode.get(check.getEmail()).equals(check.getCode());
    }

    private int generateCode() {
        return (int)(Math.random() * (90000)) + 100000;
    }

    public void exceptionTest() {
        try {
            throw new AddressException();
        } catch (MessagingException e) {
            throw new EmailSendException("이메일 전송중 오류 : " + e.getMessage(), getMethodName());
        }
    }
}
