package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.entity.itemaggregate.item.application.ItemService;
import com.sangwook.shoppingmall.entity.useraggregate.user.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.repository.ImageRepository;
import com.sangwook.shoppingmall.repository.ItemRepository;
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
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ItemRepository itemRepository;

    User user;

    @BeforeEach
    public void setUser() {

    }

    @Test
    @DisplayName("사용자는 상품을 등록할 수 있다")
    void test1() {

    }

    @Test
    @DisplayName("사용자는 본인이 올린 상품을 수정할 수 있다")
    void test2() {

    }

    @Test
    @DisplayName("사용자는 본인이 올린 상품을 삭제할 수 있다")
    void test3_1() {
    }

    @Test
    @DisplayName("사용자는 본인이 올리지 않은 상품은 삭제할 수 없다")
    void test3_2() {
    }

    @Test
    @DisplayName("사용자는 본인이올린 상품과 다른 사람들이 올린 상품목록을 확인할 수 있다")
    void test4() {

    }

    @Test
    @DisplayName("사용자는 상품 상세 페이지에서 상품의 상세 정보를 확인할 수 있다")
    void test5() {
    }

}
