package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.item.dto.ItemInfo;
import com.sangwook.shoppingmall.domain.itemImage.ItemImage;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.user.dto.UserRegister;
import com.sangwook.shoppingmall.repository.ImageRepository;
import com.sangwook.shoppingmall.repository.ItemRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", 20121122, 13,
                Gender.MALE);
        user = userService.register(userRegister);
    }

    @Test
    @DisplayName("사용자는 상품을 등록할 수 있다")
    void test1() {
        //given
        AddItem addItem = new AddItem();
        addItem.setItemName("장롱");
        addItem.setItemCount(3);
        addItem.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem.setCategory(Category.FURNITURE);
        addItem.setPrice(1_000_000);
        List<String> image = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem.setImage(image);

        //when
        Item item = itemService.add(user, addItem);
        List<ItemImage> images = imageRepository.findByItemId(item.getId());

        //then
        assertThat(item.getId()).isNotNull();
        assertThat(item.getUser()).isEqualTo(user);
        assertThat(item.getCategory()).isEqualTo(Category.FURNITURE);
        assertThat(item.getPrice()).isEqualTo(1_000_000);
        assertThat(item.getName()).isEqualTo("장롱");
        assertThat(item.getItemCount()).isEqualTo(3);
        assertThat(item.getItemInfo()).isEqualTo("장롱입니다 매우 상태가 좋습니다");

        assertThat(images).size().isEqualTo(3);

    }

    @Test
    @DisplayName("사용자는 본인이 올린 상품을 수정할 수 있다")
    void test2() {

    }

    @Test
    @DisplayName("사용자는 본인이 올린 상품을 삭제할 수 있다")
    void test3_1() {
        //given
        AddItem addItem = new AddItem();
        addItem.setItemName("장롱");
        addItem.setItemCount(3);
        addItem.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem.setCategory(Category.FURNITURE);
        addItem.setPrice(1_000_000);
        List<String> image = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem.setImage(image);
        Item item = itemService.add(user, addItem);

        //when
        itemService.delete(user, item.getId());

        //then
        Optional<Item> getItem = itemRepository.findById(item.getId());
        assertThat(getItem).isEmpty();
        assertThat(imageRepository.findByItemId(item.getId())).isEmpty();
    }

    @Test
    @DisplayName("사용자는 본인이 올리지 않은 상품은 삭제할 수 없다")
    void test3_2() {
        //given
        AddItem addItem = new AddItem();
        addItem.setItemName("장롱");
        addItem.setItemCount(3);
        addItem.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem.setCategory(Category.FURNITURE);
        addItem.setPrice(1_000_000);
        List<String> image = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem.setImage(image);
        Item item = itemService.add(user, addItem);

        UserRegister userRegister = new UserRegister("tkddnr123@naver.com",
                "상욱2", "123123",
                "01011112222", 20121122, 13,
                Gender.MALE);
        User user2 = userService.register(userRegister);

        //when

        //then
        assertThatThrownBy(() -> itemService.delete(user2, item.getId())).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("사용자는 본인이올린 상품과 다른 사람들이 올린 상품목록을 확인할 수 있다")
    void test4() {

    }

    @Test
    @DisplayName("사용자는 상품 상세 페이지에서 상품의 상세 정보를 확인할 수 있다")
    void test5() {
        //given
        AddItem addItem = new AddItem();
        addItem.setItemName("장롱");
        addItem.setItemCount(3);
        addItem.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem.setCategory(Category.FURNITURE);
        addItem.setPrice(1_000_000);
        List<String> image = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem.setImage(image);
        Item item = itemService.add(user, addItem);

        //when
        ItemInfo info = itemService.getInfo(item.getId());
        List<ItemImage> images = imageRepository.findByItemId(item.getId());

        //then
        assertThat(info.getItemId()).isEqualTo(item.getId());
        assertThat(info.getItemName()).isEqualTo(item.getName());
        assertThat(info.getPrice()).isEqualTo(item.getPrice());
        assertThat(info.getCategory()).isEqualTo(item.getCategory());
        assertThat(info.getItemCount()).isEqualTo(item.getItemCount());
        assertThat(info.getItemInfo()).isEqualTo(item.getItemInfo());

        assertThat(info.getUploadUserId()).isEqualTo(item.getUser().getId());
        assertThat(info.getUploadUserName()).isEqualTo(item.getUser().getName());

        assertThat(info.getImage().size()).isEqualTo(3);
        List<String> getLink = info.getImage();
        for (ItemImage itemImage : images) {
            assertThat(getLink.contains(itemImage.getImageLink())).isTrue();
        }

        assertThat(info.getReviewCount()).isEqualTo(0);
        assertThat(info.getReviewAverage()).isEqualTo(0.f);
        assertThat(info.getSales()).isEqualTo(0);
    }

}
