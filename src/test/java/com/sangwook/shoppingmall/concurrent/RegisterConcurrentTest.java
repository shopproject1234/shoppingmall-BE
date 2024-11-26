package com.sangwook.shoppingmall.concurrent;

import com.sangwook.shoppingmall.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class RegisterConcurrentTest {

    @Autowired private UserService userService;
    @Autowired private CleanUp cleanUp;

    /**
     * 엔티티에서 이메일로 DB 수준에서 제약 조건을 걸어주기 전에는 2개의 같은 이메일을 가진 유저가 저장되었지만 현재는 오류가 발생한다
     */
    @Test
    @DisplayName("사용자의 회원가입 요청 2건 한번에 요청")
    void test1_1() throws Exception {

        ExecutorService es = Executors.newFixedThreadPool(2);

        RegisterTask task1 = new RegisterTask(userService);
        RegisterTask task2 = new RegisterTask(userService);

        Future<User> future1 = es.submit(task1);
        Future<User> future2 = es.submit(task2);

        try {
            User user1 = future1.get();
            User user2 = future2.get();
        } catch (Throwable throwable) {
            assertThat(throwable).isInstanceOf(ExecutionException.class);
        } finally {
            es.shutdown();
        }


    }

    @AfterEach
    void tearDown() {
        cleanUp.execute();
    }
}
