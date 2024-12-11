package com.sangwook.shoppingmall.scale;

import com.sangwook.shoppingmall.application.ItemService;
import com.sangwook.shoppingmall.application.UserService;
import com.sangwook.shoppingmall.common.constant.Category;
import com.sangwook.shoppingmall.common.constant.Gender;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.AddItem;
import com.sangwook.shoppingmall.entity.useraggregate.domain.Interest;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.dto.UserRegister;
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
public class SearchScaleTest {

    @Autowired
    UserService userService;

    @Autowired
    ItemService itemService;

    User user;
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
        itemService.add(user, addItem);

        AddItem addItem2 = new AddItem();
        addItem2.setItemName("작은 장롱");
        addItem2.setItemCount(3);
        addItem2.setItemInfo("장롱입니다 크기가 작습니다");
        addItem2.setCategory(Category.FURNITURE);
        addItem2.setPrice(2_000_000);
        addItem2.setImage(image);
        itemService.add(user, addItem2);

        AddItem addItem3 = new AddItem();
        addItem3.setItemName("큰 장롱");
        addItem3.setItemCount(3);
        addItem3.setItemInfo("장롱입니다 큽니다");
        addItem3.setCategory(Category.FURNITURE);
        addItem3.setPrice(3_000_000);
        addItem3.setImage(image);
        itemService.add(user, addItem3);

        AddItem addItem4 = new AddItem();
        addItem4.setItemName("작은 조명");
        addItem4.setItemCount(1);
        addItem4.setItemInfo("조명입니다 밝습니다");
        addItem4.setCategory(Category.LIGHTING);
        addItem4.setPrice(100_000);
        addItem4.setImage(image);
        itemService.add(user, addItem4);

        AddItem addItem5 = new AddItem();
        addItem5.setItemName("큰 조명");
        addItem5.setItemCount(1);
        addItem5.setItemInfo("조명입니다 크기가 큽니다");
        addItem5.setCategory(Category.LIGHTING);
        addItem5.setPrice(200_000);
        addItem5.setImage(image);
        itemService.add(user, addItem5);
    }

    @DisplayName("상품 목록을 키워드와 함께 조회 시 검색된 상품에 가장 많이 해당되는 카테고리의 관심도(scale)를 1 증가시킨다")
    @Test
    void test() {
        //given

        //when
        itemService.getList(1, "latest", "장롱", null, user);

        //then
        User getUser = userRepository.findUserFetchInterest(user.getId()).get();
        List<Interest> interests = getUser.getInterests();

        assertThat(interests.get(0).getCategory()).isEqualTo(Category.FURNITURE);
        assertThat(interests.get(0).getScale()).isEqualTo(1);
    }
}
