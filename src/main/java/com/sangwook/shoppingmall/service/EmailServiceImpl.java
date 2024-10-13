package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.entity.useraggregate.user.domain.dto.EmailCheck;
import com.sangwook.shoppingmall.exception.custom.EmailSendException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
@Primary
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    @Value("${spring.mail.username}")
    private String sender;

    @Async
    @Override
    public void sendMail(String email) {
        int code = generateCode();
        MimeMessage mail = createMail(email, code);
        javaMailSender.send(mail);
    }

    @Override
    public Boolean verifyCode(EmailCheck check) {
        if (redisService.getCode(check.getEmail()).equals(check.getCode())) { // getCode에서 값이 없을경우 -1 반환됨
            redisService.verified(check.getEmail()); // 인증 될경우 코드 1로 변경됨
            return true;
        }
        return false;
    }

    private MimeMessage createMail(String email, int code) {

        saveEmailCode(email, code); //유지 시간 10분으로 redis 저장소에 email, code를 key value값으로 저장

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(sender);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("ShopProject : 이메일 인증 코드입니다");

            String body = "Authentication Code : " + code;
            message.setText(body);
        } catch (MessagingException e) {
            //MessageException은 checkedException이므로 잡아서 Runtime으로 처리
            throw new EmailSendException("이메일 전송중 오류 : " + e.getMessage(), getMethodName());
        }
        return message;
    }

    private void saveEmailCode(String email, Integer code) {
        //이미 Redis에 해당 이메일을 Key로 하는 데이터가 있을 경우 지우고 새로 생성
        Integer getCode = redisService.getCode(email);
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
}
