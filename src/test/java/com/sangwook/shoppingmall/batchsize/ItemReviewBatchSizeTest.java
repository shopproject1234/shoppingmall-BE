package com.sangwook.shoppingmall.batchsize;

import com.sangwook.shoppingmall.common.constant.Category;
import com.sangwook.shoppingmall.common.constant.Gender;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.dto.AddCart;
import com.sangwook.shoppingmall.application.ItemService;
import com.sangwook.shoppingmall.application.ReviewService;
import com.sangwook.shoppingmall.entity.itemaggregate.review.dto.ReviewWrite;
import com.sangwook.shoppingmall.entity.itemaggregate.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.domain.Review;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.AddItem;
import com.sangwook.shoppingmall.entity.itemaggregate.item.infra.ItemRepository;
import com.sangwook.shoppingmall.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.dto.UserRegister;
import com.sangwook.shoppingmall.service.fake.FakeCartService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class ItemReviewBatchSizeTest {

    /**
     * 잘못된 테스트 코드, OneToMany 관계에서 N+1일 문제가 발생하는 경우는 여러개의 Item에 소속된 여러개의 Review를
     * 조회할 때 발생한다 하지만 review를 삭제하고, review를 수정하는 코드에서는 Item 객체가 1개로 고정되어 있는 상태에서
     * Review 탐색을 진행하므로 Item 객체 내부의 reviews 컬렉션에 대해서만 1번의 쿼리가 더 나갈 뿐 N+1 문제와는 무관하다
     * 그렇기에 @BatchSize를 적용하는 것 자체가 의미가 없다
     *
     * 적절하게 적용하려면?
     * 만약 Item 객체를 20개 불러온다고 가정해보면
     * 각각의 Item에 대한 실제 review를 가져오려면 Item이 20개이므로 총 20번의 쿼리가 나가게 된다
     * 이런 경우에 BatchSize를 사용한다
     * SELECT * FROM Review WHERE item_id IN (1, 2, 3, ..., 10);와 같이 in 절을 사용하여 쿼리의 수를 줄이게된다
     */

    User user;
    Item item;

    @Autowired
    UserService userService;
    @Autowired
    ItemService itemService;
    @Autowired
    FakeCartService cartService;
    @Autowired
    ReviewService reviewService;

    @PersistenceContext
    EntityManager em;
    @Autowired
    ItemRepository itemRepository;

    User newUser1;
    User newUser2;
    User newUser3;
    User newUser4;


    @BeforeEach
    void set() {
        UserRegister userRegister = new UserRegister("tkddnr12@naver.com",
                "상욱", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        user = userService.register(userRegister);

        AddItem addItem = new AddItem();
        addItem.setItemName("장롱");
        addItem.setItemCount(30);
        addItem.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem.setCategory(Category.FURNITURE);
        addItem.setPrice(1_000_000);
        List<String> image = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem.setImage(image);
        item = itemService.add(user, addItem);

        UserRegister newRegister = new UserRegister("tkddnr123@naver.com",
                "쌍욱1", "123123",
                "01033333333", "2012-11-22",
                Gender.MALE, List.of());
        newUser1 = userService.register(newRegister);
        newUser2 = userService.register(new UserRegister("tkddnr1234@naver.com",
                "쌍욱2", "123123",
                "01033333333", "2012-11-22",
                Gender.MALE, List.of()));
        newUser3 = userService.register(new UserRegister("tkddnr12345@naver.com",
                "쌍욱3", "123123",
                "01033333333", "2012-11-22",
                Gender.MALE, List.of()));

        newUser4 = userService.register(new UserRegister("tkddnr123456@naver.com",
                "쌍욱3", "123123",
                "01033333333", "2012-11-22",
                Gender.MALE, List.of()));

    }

    /**
     * 해당 테스트의 내용은 노션에 작성됨
     */
    @Test
    @DisplayName("여러 사용자가 1개의 아이템에 대한 리뷰를 작성, Item 도메인에 설정한 BatchSize만큼 Review 정보가 가져와지는지 확인")
    void test1() {
        //given
        ReviewWrite reviewWrite = new ReviewWrite();
        reviewWrite.setContent("상태가 좋군요");
        reviewWrite.setScore(4.5f);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        cartService.add(newUser1.getId(), addCart);
        cartService.add(newUser2.getId(), addCart);
        cartService.add(newUser3.getId(), addCart);
        cartService.add(newUser4.getId(), addCart);

        cartService.order(newUser1.getId());
        cartService.order(newUser2.getId());
        cartService.order(newUser3.getId());
        cartService.order(newUser4.getId());

        reviewService.reviewWrite(newUser1.getId(), item.getId(), reviewWrite);
        reviewService.reviewWrite(newUser2.getId(), item.getId(), reviewWrite);
        reviewService.reviewWrite(newUser3.getId(), item.getId(), reviewWrite);
        Review review = reviewService.reviewWrite(newUser4.getId(), item.getId(), reviewWrite);

        em.flush();
        Long reviewId = review.getId();
        em.clear();


        //when
        reviewService.deleteReview(newUser4, reviewId, item.getId());
    }
}
