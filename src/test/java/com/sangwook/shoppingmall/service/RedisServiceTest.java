package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.application.RedisService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @DisplayName("key value 지정")
    @Test
    void test1() {
        //given
        redisService.setValues("email@naver.com", 133133, Duration.ofMinutes(10));

        //when
        Integer code = redisService.getCode("email@naver.com");

        //then
        assertThat(code).isEqualTo(133133);
    }

    @DisplayName("데이터 삭제시 code는 -1로 변경됨")
    @Test
    void test2() {
        //given
        redisService.setValues("email@naver.com", 133133, Duration.ofMinutes(10));

        //when
        redisService.deleteValues("email@naver.com");
        Integer code = redisService.getCode("email@naver.com");

        //then
        assertThat(code).isEqualTo(-1);
    }

    @DisplayName("인증된 이메일일 경우 code가 1로 변경됨")
    @Test
    void test3() {
        //given
        redisService.setValues("email@naver.com", 133133, Duration.ofMinutes(10));
        redisService.verified("email@naver.com");

        //when
        Integer code = redisService.getCode("email@naver.com");

        //then
        assertThat(code).isEqualTo(1);
    }
}
