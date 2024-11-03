package com.sangwook.shoppingmall.nplusone;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.AddCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.MyCart;
import com.sangwook.shoppingmall.entity.itemaggregate.item.application.ItemService;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.AddItem;
import com.sangwook.shoppingmall.entity.useraggregate.user.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.dto.UserRegister;
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

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CartNPlusOneTest {

    @Autowired private UserService userService;
    @Autowired private FakeCartService cartService;
    @Autowired private ItemService itemService;
    @Autowired
    EntityManager em;

    Item item1;
    Item item2;
    Item item3;
    User user;

    @BeforeEach
    public void set() {
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        user = userService.register(userRegister);

        AddItem addItem1 = new AddItem();
        addItem1.setItemName("장롱");
        addItem1.setItemCount(3);
        addItem1.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem1.setCategory(Category.FURNITURE);
        addItem1.setPrice(1_000_000);
        List<String> image1 = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem1.setImage(image1);
        item1 = itemService.add(user, addItem1);

        AddItem addItem2 = new AddItem();
        addItem2.setItemName("장롱2");
        addItem2.setItemCount(3);
        addItem2.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem2.setCategory(Category.FURNITURE);
        addItem2.setPrice(1_000_000);
        List<String> image2 = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem2.setImage(image2);
        item2 = itemService.add(user, addItem2);

        AddItem addItem3 = new AddItem();
        addItem3.setItemName("장롱3");
        addItem3.setItemCount(3);
        addItem3.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem3.setCategory(Category.FURNITURE);
        addItem3.setPrice(1_000_000);
        List<String> image3 = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem3.setImage(image3);
        item3 = itemService.add(user, addItem3);
    }

    /**
     * getMyCart 메서드에서 MyCart 객체의 cart.getItem.xxx를 실행할 때 Item에 대한 N+1 문제가 발생한다
     */
    @Test
    @DisplayName("본인의 장바구니 상품 조회 - N+1 확인용 -> 발생")
    void test1() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User user = userService.register(userRegister);

        AddCart addCart1 = new AddCart();
        addCart1.setItemId(item1.getId());
        addCart1.setItemCount(3);
        cartService.add(user.getId(), addCart1);

        AddCart addCart2 = new AddCart();
        addCart2.setItemId(item2.getId());
        addCart2.setItemCount(3);
        cartService.add(user.getId(), addCart2);

        AddCart addCart3 = new AddCart();
        addCart3.setItemId(item3.getId());
        addCart3.setItemCount(3);
        cartService.add(user.getId(), addCart3);

        em.flush();
        em.clear();

        //when
        List<MyCart> myCart = cartService.getMyCartNPlusOne(user.getId());

    }

    @Test
    @DisplayName("본인의 장바구니 상품 조회 - N+1 해결")
    void test2() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User user = userService.register(userRegister);

        AddCart addCart1 = new AddCart();
        addCart1.setItemId(item1.getId());
        addCart1.setItemCount(3);
        cartService.add(user.getId(), addCart1);

        AddCart addCart2 = new AddCart();
        addCart2.setItemId(item2.getId());
        addCart2.setItemCount(3);
        cartService.add(user.getId(), addCart2);

        AddCart addCart3 = new AddCart();
        addCart3.setItemId(item3.getId());
        addCart3.setItemCount(3);
        cartService.add(user.getId(), addCart3);

        em.flush();
        em.clear();

        //when
        List<MyCart> myCart = cartService.getMyCart(user.getId());

    }

    @Test
    @DisplayName("장바구니 최종 order - N+1 확인용 -> 발생")
    void test3() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User user = userService.register(userRegister);

        AddCart addCart1 = new AddCart();
        addCart1.setItemId(item1.getId());
        addCart1.setItemCount(3);
        cartService.add(user.getId(), addCart1);

        AddCart addCart2 = new AddCart();
        addCart2.setItemId(item2.getId());
        addCart2.setItemCount(3);
        cartService.add(user.getId(), addCart2);

        AddCart addCart3 = new AddCart();
        addCart3.setItemId(item3.getId());
        addCart3.setItemCount(3);
        cartService.add(user.getId(), addCart3);

        em.flush();
        em.clear();

        //when
        cartService.orderNPlusOne(user.getId());
    }

    @Test
    @DisplayName("장바구니 최종 order - N+1 해결")
    void test4() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User user = userService.register(userRegister);

        AddCart addCart1 = new AddCart();
        addCart1.setItemId(item1.getId());
        addCart1.setItemCount(3);
        cartService.add(user.getId(), addCart1);

        AddCart addCart2 = new AddCart();
        addCart2.setItemId(item2.getId());
        addCart2.setItemCount(3);
        cartService.add(user.getId(), addCart2);

        AddCart addCart3 = new AddCart();
        addCart3.setItemId(item3.getId());
        addCart3.setItemCount(3);
        cartService.add(user.getId(), addCart3);

        em.flush();
        em.clear();

        //when
        cartService.order(user.getId());
    }
}
