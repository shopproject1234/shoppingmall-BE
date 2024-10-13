package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.entity.cartaggregate.cart.application.CartService;
import com.sangwook.shoppingmall.entity.itemaggregate.item.application.ItemService;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.useraggregate.user.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.repository.CartRepository;
import com.sangwook.shoppingmall.repository.HistoryRepository;
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
public class CartServiceTest {

    @Autowired private UserService userService;
    @Autowired private ItemService itemService;
    @Autowired private CartService cartService;
    @Autowired private CartRepository cartRepository;
    @Autowired private HistoryRepository historyRepository;

    User user;
    Item item;

    @BeforeEach
    public void set() {

    }

    @Test
    @DisplayName("사용자는 원하는 상품을 장바구니에 저장할 수 있다")
    void test1_1() {

    }

    @Test
    @DisplayName("사용자는 이미 담긴 상품을 장바구니에 또 담을 경우 카운트가 올라가게 된다")
    void test1_2() {

    }

    @Test
    @DisplayName("사용자는 자신이 올린 상품을 장바구니에 담을 수 없다")
    void test1_3() {

    }

    @Test
    @DisplayName("장바구니에 등록된 상품을 삭제할 수 있다")
    void test2() {


    }

    @Test
    @DisplayName("장바구니에 등록된 상품을 조회할 수 있다")
    void test3() {

    }

    @Test
    @DisplayName("장바구니에 있는 상품들을 최종적으로 주문할 수 있다")
    void test4_1() {

    }

    @Test
    @DisplayName("장바구니에 있는 상품들을 주문하면 결제내역에 저장된다")
    void test4_2() {

    }
}
