package com.sangwook.shoppingmall.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ReviewServiceTest {

    @Test
    @DisplayName("사용자는 구매한 상품에 리뷰를 작성할 수 있다")
    void test1_1() {

    }

    @Test
    @DisplayName("구매하지 않은 상품에 대해서는 리뷰를 작성할 수 없다")
    void test1_2() {

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
