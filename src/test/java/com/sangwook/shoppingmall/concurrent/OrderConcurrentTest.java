package com.sangwook.shoppingmall.concurrent;

import com.sangwook.shoppingmall.entity.itemaggregate.item.application.ItemService;
import com.sangwook.shoppingmall.entity.itemaggregate.item.infra.ItemRepository;
import com.sangwook.shoppingmall.entity.useraggregate.user.application.UserService;
import com.sangwook.shoppingmall.service.fake.FakeCartService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"test", "orderTest"})
@AutoConfigureMockMvc
@Transactional
public class OrderConcurrentTest {

    @Autowired
    FakeCartService cartService;
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;
    @Autowired EntityManager em;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CleanUp cleanUp;

    /**
     * 상품주문에서의 동시성 문제 발생
     * 테스트 설명 : 수량이 3인 상품에 대해서 사용자 1이 2개 주문, 사용자 2가 2개 주문을 동시에 진행한다
     * 동시성 문제 발생 : 각 쓰레드가 동일한 초기 수량인 3개를 기준으로 주문을 처리하여 두 요청 모두 order의 로직을 통과하게 되어 사용자 1도 구매처리, 사용자 2도 구매처리가 되었다
     * 하지만 남은 수량은 1개가 되었다...
     *
     * 간단하게 해결하는 방법은 Item 객체에 version을 두어 Optimistic Lock을 적용하는것이다
     */
    @Test
    @DisplayName("상품을 동시에 주문할 때의 동시성 테스트")
    void test1() {

        //when
        ExecutorService es = Executors.newFixedThreadPool(2);

        OrderTask task1 = new OrderTask(cartService, 2L);
        OrderTask task2 = new OrderTask(cartService, 3L);

        Future<?> future1 = es.submit(task1);
        Future<?> future2 = es.submit(task2);

        try {
            Object o = future1.get();
            Object o1 = future2.get();
        } catch (Throwable throwable) {
            assertThat(throwable).isInstanceOf(ExecutionException.class);
            assertThat(throwable.getCause()).isInstanceOf(ObjectOptimisticLockingFailureException.class);
        }

    }

    @AfterEach
    void tearDown() {
        cleanUp.execute();
    }
}
