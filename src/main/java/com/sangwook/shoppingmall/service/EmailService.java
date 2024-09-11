package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.domain.user.dto.EmailCheck;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    public void sendMail(String email) {
        int code = generateCode();
        MimeMessage mail = createMail(email, code);
        javaMailSender.send(mail);
    }

    private MimeMessage createMail(String email, int code) {

        saveEmailCode(email, code);

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(sender);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("ShopProject : 이메일 인증 코드입니다");

            String body = "Authentication Code : " + code;
            message.setText(body);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    private void saveEmailCode(String email, Integer code) {
        //이미 Redis에 해당 이메일을 Key로 하는 데이터가 있을 경우 지우고 새로 생성
        Integer getCode = redisService.getCode(email);
        log.info("code={}", getCode);
        if (getCode.equals(-1)) {
            redisService.setValues(email, code, Duration.ofMinutes(10));
        } else {
            redisService.deleteValues(email);
            redisService.setValues(email, code, Duration.ofMinutes(10));
        }
    }

    private int generateCode() {
        return (int)(Math.random() * (90000)) + 100000;
    }


    public Boolean checkCode(EmailCheck check) {
        return redisService.getCode(check.getEmail()).equals(check.getCode());
    }
}
