package com.sangwook.shoppingmall.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Transactional
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Integer> redisTemplate;

    public void setValues(String email, Integer code, Duration duration) {
        ValueOperations<String, Integer> values = redisTemplate.opsForValue();
        values.set(email, code, duration);
    }

    public void deleteValues(String email) {
        redisTemplate.delete(email);
    }

    //code가 있을 경우 code 반환, 없을 경우 -1 반환
    public Integer getCode(String email) {
        ValueOperations<String, Integer> values = redisTemplate.opsForValue();
        if (values.get(email) == null) {
            return -1;
        }
        return values.get(email);
    }

    //인증이 된 경우 code 값을 1로 변경
    public void verified(String email) {
        ValueOperations<String, Integer> values = redisTemplate.opsForValue();
        values.getAndSet(email, 1);
    }
}
