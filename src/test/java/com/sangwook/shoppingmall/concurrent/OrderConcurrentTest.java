package com.sangwook.shoppingmall.concurrent;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.AddCart;
import com.sangwook.shoppingmall.entity.itemaggregate.item.application.ItemService;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.AddItem;
import com.sangwook.shoppingmall.entity.useraggregate.user.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.dto.UserRegister;
import com.sangwook.shoppingmall.service.fake.FakeCartService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class OrderConcurrentTest {

    @Autowired
    FakeCartService cartService;
    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;
    @PersistenceContext
    EntityManager em;

    User user;
    Item item;

    @BeforeEach
    public void set() {
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
    }

    @Test
    @DisplayName("상품을 동시에 주문할 때의 동시성 테스트")
    void test1() throws ExecutionException, InterruptedException {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User customer1 = userService.register(userRegister);

        User customer2 = userService.register(new UserRegister("tatata@naver.com",
                "상욱3", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of()));

        //상품은 3개인데 2개 2개씩 사용자가 카트에 추가하여 주문한다
        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(2);


        //when
        ExecutorService es = Executors.newFixedThreadPool(2);

        OrderTask task1 = new OrderTask(cartService, customer1.getId(), addCart);
        OrderTask task2 = new OrderTask(cartService, customer2.getId(), addCart);

        Future<?> future1 = es.submit(task1);
        Future<?> future2 = es.submit(task2);

        Object o = future1.get();
        Object o1 = future2.get();

        assertThat(item.getItemCount()).isEqualTo(-1);


    }
}
