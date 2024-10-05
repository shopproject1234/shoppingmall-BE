package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.domain.cart.dto.AddCart;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.review.Review;
import com.sangwook.shoppingmall.domain.review.dto.ReviewList;
import com.sangwook.shoppingmall.domain.review.dto.ReviewWrite;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.user.dto.UserRegister;
import com.sangwook.shoppingmall.exception.custom.ObjectNotFoundException;
import com.sangwook.shoppingmall.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", 20121122,
                Gender.MALE);
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

        UserRegister newRegister = new UserRegister("tkddnr123@naver.com",
                "쌍욱", "123123",
                "01033333333", 20121122,
                Gender.MALE);
        newUser = userService.register(newRegister);
    }

    @Test
    @DisplayName("사용자는 구매한 상품에 리뷰를 작성할 수 있다")
    void test1_1() {
        //given
        ReviewWrite reviewWrite = new ReviewWrite();
        reviewWrite.setContent("상태가 좋군요");
        reviewWrite.setScore(4.5f);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        cartService.add(newUser.getId(), addCart);

        cartService.order(newUser.getId());

        //when
        Review review = reviewService.reviewWrite(newUser.getId(), item.getId(), reviewWrite);

        //then
        assertThat(review.getContent()).isEqualTo("상태가 좋군요");
        assertThat(review.getScore()).isEqualTo(4.5f);
        assertThat(review.getUser()).isEqualTo(newUser);
        assertThat(review.getItem()).isEqualTo(item);

    }

    @Test
    @DisplayName("구매하지 않은 상품에 대해서는 리뷰를 작성할 수 없다")
    void test1_2() {
        //given
        ReviewWrite reviewWrite = new ReviewWrite();
        reviewWrite.setContent("상태가 좋군요");
        reviewWrite.setScore(4.5f);

        //then
        assertThatThrownBy(() -> reviewService.reviewWrite(newUser.getId(), item.getId(), reviewWrite)).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    @DisplayName("사용자는 상품에 대한 리뷰목록을 확인할 수 있다")
    void test2() {
        //given
        ReviewWrite reviewWrite = new ReviewWrite();
        reviewWrite.setContent("상태가 좋군요");
        reviewWrite.setScore(4.5f);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        cartService.add(newUser.getId(), addCart);

        cartService.order(newUser.getId());

        Review review = reviewService.reviewWrite(newUser.getId(), item.getId(), reviewWrite);

        //when
        Page<ReviewList> getList = reviewService.findReview(item.getId(), 1);

        //then
        assertThat(getList.getNumberOfElements()).isEqualTo(1);
        assertThat(getList.getContent().get(0).getReviewId()).isEqualTo(review.getId());
        assertThat(getList.getContent().get(0).getContent()).isEqualTo("상태가 좋군요");
        assertThat(getList.getContent().get(0).getUserId()).isEqualTo(newUser.getId());
        assertThat(getList.getContent().get(0).getUsername()).isEqualTo(newUser.getName());
        assertThat(getList.getContent().get(0).getScore()).isEqualTo(4.5f);

    }

    @Test
    @DisplayName("사용자는 본인이 작성한 리뷰를 수정할 수 있다")
    void test3() {
        //given
        ReviewWrite reviewWrite = new ReviewWrite();
        reviewWrite.setContent("상태가 좋군요");
        reviewWrite.setScore(4.5f);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        cartService.add(newUser.getId(), addCart);

        cartService.order(newUser.getId());

        Review review = reviewService.reviewWrite(newUser.getId(), item.getId(), reviewWrite);

        //when
        ReviewWrite update = new ReviewWrite();
        update.setScore(3f);
        update.setContent("배송이 너무 느렸어요");

        review = reviewService.updateReview(newUser, review.getId(), update);

        //then
        assertThat(review.getContent()).isEqualTo("배송이 너무 느렸어요");
        assertThat(review.getScore()).isEqualTo(3f);
        assertThat(review.getUser()).isEqualTo(newUser);
        assertThat(review.getItem()).isEqualTo(item);
    }

    @Test
    @DisplayName("사용자는 본인이 작성한 리뷰를 삭제할 수 있다")
    void test4() {
        //given
        ReviewWrite reviewWrite = new ReviewWrite();
        reviewWrite.setContent("상태가 좋군요");
        reviewWrite.setScore(4.5f);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        cartService.add(newUser.getId(), addCart);

        cartService.order(newUser.getId());

        Review review = reviewService.reviewWrite(newUser.getId(), item.getId(), reviewWrite);

        //when
        reviewService.deleteReview(newUser, review.getId());

        //then
        Optional<Review> get = reviewRepository.findById(review.getId());
        assertThat(get).isEmpty();
    }
}
