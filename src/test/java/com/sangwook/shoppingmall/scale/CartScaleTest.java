package com.sangwook.shoppingmall.scale;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.AddCart;
import com.sangwook.shoppingmall.entity.itemaggregate.item.application.ItemService;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.AddItem;
import com.sangwook.shoppingmall.entity.useraggregate.user.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.Interest;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.dto.UserRegister;
import com.sangwook.shoppingmall.entity.useraggregate.user.infra.UserRepository;
import com.sangwook.shoppingmall.service.fake.FakeCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CartScaleTest {

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    User user;
    Item item1;
    Item item2;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    FakeCartService cartService;

    @BeforeEach
    void set() {
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, new ArrayList<>());
        user = userService.register(userRegister);

        AddItem addItem = new AddItem();
        addItem.setItemName("장롱");
        addItem.setItemCount(3);
        addItem.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem.setCategory(Category.FURNITURE);
        addItem.setPrice(1_000_000);
        List<String> image = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem.setImage(image);
        item1 = itemService.add(user, addItem);

        AddItem addItem5 = new AddItem();
        addItem5.setItemName("큰 조명");
        addItem5.setItemCount(1);
        addItem5.setItemInfo("조명입니다 크기가 큽니다");
        addItem5.setCategory(Category.LIGHTING);
        addItem5.setPrice(200_000);
        addItem5.setImage(image);
        item2 = itemService.add(user, addItem5);
    }

    @DisplayName("상품을 장바구니에 추가 시 해당 상품에 맞는 카테고리의 관심도(scale)를 10 증가시킨다")
    @Test
    void test() {
        //given
        UserRegister userRegister = new UserRegister("tkddnr123123@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, new ArrayList<>());
        User newUser = userService.register(userRegister);

        //when
        AddCart addCart = new AddCart();
        addCart.setItemCount(2);
        addCart.setItemId(item1.getId());
        cartService.add(newUser.getId(), addCart);

        //then
        User getUser = userRepository.findUserFetchInterest(newUser.getId()).get();
        List<Interest> interests = getUser.getInterests();

        assertThat(interests.get(0).getCategory()).isEqualTo(Category.FURNITURE);
        assertThat(interests.get(0).getScale()).isEqualTo(10);
    }
}
