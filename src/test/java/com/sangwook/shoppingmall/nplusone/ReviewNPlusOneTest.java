package com.sangwook.shoppingmall.nplusone;

import com.sangwook.shoppingmall.common.constant.Category;
import com.sangwook.shoppingmall.common.constant.Gender;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.dto.AddCart;
import com.sangwook.shoppingmall.application.ItemService;
import com.sangwook.shoppingmall.application.ReviewService;
import com.sangwook.shoppingmall.entity.itemaggregate.review.dto.ReviewWrite;
import com.sangwook.shoppingmall.entity.itemaggregate.review.infra.ReviewRepository;
import com.sangwook.shoppingmall.entity.itemaggregate.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.domain.Review;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.AddItem;
import com.sangwook.shoppingmall.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.dto.UserRegister;
import com.sangwook.shoppingmall.service.fake.FakeCartService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository에서 불러오는 방식에 따른 N+1 문제 발생을 확인하는 테스트이다.
 */
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ReviewNPlusOneTest {

    @Autowired
    private UserService userService;
    @Autowired private ItemService itemService;
    @Autowired private ReviewService reviewService;
    @Autowired private FakeCartService cartService;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private EntityManager em;

    User user;
    Item item;
    User newUser1;
    User newUser2;
    User newUser3;

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

        UserRegister newRegister1 = new UserRegister("tkddnr123@naver.com",
                "쌍욱", "123123",
                "01033333333", "2012-11-22",
                Gender.MALE, List.of());
        newUser1 = userService.register(newRegister1);

        UserRegister newRegister2 = new UserRegister("tkddnr1234@naver.com",
                "싱욱", "123123",
                "01033333333", "2012-11-22",
                Gender.MALE, List.of());
        newUser2 = userService.register(newRegister2);

        UserRegister newRegister3 = new UserRegister("tkddnr12345@naver.com",
                "손상욱", "123123",
                "01033333333", "2012-11-22",
                Gender.MALE, List.of());
        newUser3 = userService.register(newRegister3);
    }

    /**
     * N+1 발생
     * Item 객체의 getReviewWithUser() 메서드에서 N+1 문제가 발생하였다
     */
    @Test
    @DisplayName("사용자는 본인이 작성한 리뷰를 삭제할 수 있다 - 기본적인 deleteReview() 사용")
    void test1() {
        //given
        ReviewWrite reviewWrite = new ReviewWrite();
        reviewWrite.setContent("상태가 좋군요");
        reviewWrite.setScore(4.5f);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(1);
        cartService.add(newUser1.getId(), addCart);
        cartService.add(newUser2.getId(), addCart);
        cartService.add(newUser3.getId(), addCart);

        cartService.order(newUser1.getId());
        cartService.order(newUser2.getId());
        cartService.order(newUser3.getId());

        reviewService.reviewWrite(newUser1.getId(), item.getId(), reviewWrite);
        reviewService.reviewWrite(newUser2.getId(), item.getId(), reviewWrite);
        Review review = reviewService.reviewWrite(newUser3.getId(), item.getId(), reviewWrite); //총 3개의 리뷰 저장
        em.flush();

        Long reviewId = review.getId();
        //when
        em.clear();
        /**
         * 테스트코드에서는 이미 해당 Item의 정보과 Review의 정보를 불러왔던 내용이 있기 때문에 영속성 컨텍스트에 캐싱이 되어있는 상태이다
         * 해당 내용들을 지워주기 위해 em.clear로 영속성 컨텍스트를 비워준 결과 N+1문제가 발생하였다
         */
        reviewService.deleteReview(newUser3, reviewId, item.getId());
        em.flush();

        Optional<Review> getReview = reviewRepository.findById(reviewId);
        assertThat(getReview).isEmpty();
    }

    /**
     * 사용된 쿼리 "select i from Item i join fetch i.reviews r join fetch r.user where i.id=:itemId" -> 변경 "select i from Item i join fetch i.reviews where i.id=:itemId"
     * item 객체안의 reviews의 user를 한꺼번에 fetch 조인한다 -> item 객체안의 review를 한꺼번에 조인
     */
    @Test
    @DisplayName("사용자는 본인이 작성한 리뷰를 삭제할 수 있다 - deleteReviewFetch() 사용")
    void test2() {
        //given
        ReviewWrite reviewWrite = new ReviewWrite();
        reviewWrite.setContent("상태가 좋군요");
        reviewWrite.setScore(4.5f);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(1);
        cartService.add(newUser1.getId(), addCart);
        cartService.add(newUser2.getId(), addCart);
        cartService.add(newUser3.getId(), addCart);

        cartService.order(newUser1.getId());
        cartService.order(newUser2.getId());
        cartService.order(newUser3.getId());

        reviewService.reviewWrite(newUser1.getId(), item.getId(), reviewWrite);
        reviewService.reviewWrite(newUser2.getId(), item.getId(), reviewWrite);
        Review review = reviewService.reviewWrite(newUser3.getId(), item.getId(), reviewWrite); //총 3개의 리뷰 저장
        em.flush();

        Long reviewId = review.getId();
        //when
        em.clear();

        reviewService.deleteReviewFetch(newUser3, reviewId, item.getId());
        em.flush();

        Optional<Review> getReview = reviewRepository.findById(reviewId);
        assertThat(getReview).isEmpty();
    }
}
