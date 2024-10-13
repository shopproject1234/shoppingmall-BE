package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.entity.cartaggregate.cart.application.CartService;
import com.sangwook.shoppingmall.entity.itemaggregate.item.application.ItemService;
import com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.application.ReviewService;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.useraggregate.user.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ReviewServiceTest {

    @Autowired private UserService userService;
    @Autowired private ItemService itemService;
    @Autowired private ReviewService reviewService;
    @Autowired private CartService cartService;
    @Autowired private ReviewRepository reviewRepository;


    User user;
    Item item;
    User newUser;

    @BeforeEach
    void set() {
    }

    @Test
    @DisplayName("사용자는 구매한 상품에 리뷰를 작성할 수 있다")
    void test1_1() {

    }

    @Test
    @DisplayName("구매하지 않은 상품에 대해서는 리뷰를 작성할 수 없다")
    void test1_2() {
    }

    @Test
    @DisplayName("구매한 상품 1개당 1개의 리뷰만 작성할 수 있다")
    void test1_3() {

    }

    @Test
    @DisplayName("사용자는 상품에 대한 리뷰목록을 확인할 수 있다")
    void test2() {

    }

    @Test
    @DisplayName("사용자는 본인이 작성한 리뷰를 수정할 수 있다")
    void test3() {
    }

    @Test
    @DisplayName("사용자는 본인이 작성한 리뷰를 삭제할 수 있다")
    void test4() {
    }
}
