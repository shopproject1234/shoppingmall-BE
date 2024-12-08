package com.sangwook.shoppingmall.scale;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.entity.itemaggregate.item.application.ItemService;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.AddItem;
import com.sangwook.shoppingmall.entity.useraggregate.user.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.Interest;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.dto.UserRegister;
import com.sangwook.shoppingmall.entity.useraggregate.user.infra.UserRepository;
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
public class InfoScaleTest {

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    User user;
    Item item1;
    Item item2;

    @Autowired
    private UserRepository userRepository;

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

    @DisplayName("상품 상세 조회 시 해당 상품에 맞는 카테고리의 관심도(scale)를 2 증가시킨다")
    @Test
    void test() {
        //given

        //when
        itemService.getInfo(item1.getId(), user);

        //then
        User getUser = userRepository.findUserFetchInterest(user.getId()).get();
        List<Interest> interests = getUser.getInterests();

        assertThat(interests.get(0).getCategory()).isEqualTo(Category.FURNITURE);
        assertThat(interests.get(0).getScale()).isEqualTo(2);
    }
}
