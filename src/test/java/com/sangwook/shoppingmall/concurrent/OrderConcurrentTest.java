package com.sangwook.shoppingmall.concurrent;

import com.sangwook.shoppingmall.application.ItemService;
import com.sangwook.shoppingmall.common.constant.Category;
import com.sangwook.shoppingmall.common.constant.Gender;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.dto.AddCart;
import com.sangwook.shoppingmall.entity.itemaggregate.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.AddItem;
import com.sangwook.shoppingmall.entity.itemaggregate.item.infra.ItemRepository;
import com.sangwook.shoppingmall.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.dto.UserRegister;
import com.sangwook.shoppingmall.service.fake.FakeCartService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class OrderConcurrentTest {

    @Autowired
    FakeCartService cartService;
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    CleanUp cleanUp;

    User user;
    Item item;
    Long customer1Id;
    Long customer2Id;

    @Transactional
    @BeforeEach
    void set() {
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        user = userService.register(userRegister);

        AddItem addItem = new AddItem();
        addItem.setItemName("장롱");
        addItem.setItemCount(3);
        addItem.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem.setCategory(Category.FURNITURE);
        addItem.setPrice(1_000_000);
        List<String> image = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem.setImage(image);
        item = itemService.add(user, addItem);

        UserRegister userRegister2 = new UserRegister("taaaa141414@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User customer1 = userService.register(userRegister2);

        User customer2 = userService.register(new UserRegister("tatata123123@naver.com",
                "상욱3", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of()));

        //상품은 3개인데 2개 2개씩 사용자가 카트에 추가하여 주문한다
        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(2);

        customer1Id = customer1.getId();
        customer2Id = customer2.getId();

        cartService.add(customer1.getId(), addCart);
        cartService.add(customer2.getId(), addCart);
    }

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

        OrderTask task1 = new OrderTask(cartService, customer1Id);
        OrderTask task2 = new OrderTask(cartService, customer2Id);

        Future<?> future1 = es.submit(task1);
        Future<?> future2 = es.submit(task2);

        try {
            Object o = future1.get();
            Object o1 = future2.get();
        } catch (Throwable throwable) {
            assertThat(throwable).isInstanceOf(ExecutionException.class);
            assertThat(throwable.getCause()).isInstanceOf(ObjectOptimisticLockingFailureException.class);
        } finally {
            es.shutdown();
        }

    }

    @AfterEach
    void tearDown() {
        cleanUp.execute();
    }
}
