package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.common.constant.Category;
import com.sangwook.shoppingmall.common.constant.Gender;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.dto.AddCart;
import com.sangwook.shoppingmall.application.ItemService;
import com.sangwook.shoppingmall.entity.itemaggregate.itemImage.infra.ImageRepository;
import com.sangwook.shoppingmall.application.ReviewService;
import com.sangwook.shoppingmall.entity.itemaggregate.review.dto.ReviewWrite;
import com.sangwook.shoppingmall.entity.itemaggregate.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.domain.ItemImage;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.AddItem;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.ItemInfo;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.ItemList;
import com.sangwook.shoppingmall.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import com.sangwook.shoppingmall.entity.itemaggregate.item.infra.ItemRepository;
import com.sangwook.shoppingmall.entity.useraggregate.user.dto.UserRegister;
import com.sangwook.shoppingmall.exception.custom.UserValidationException;
import com.sangwook.shoppingmall.service.fake.FakeCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private ItemRepository itemRepository;

    @Autowired private ImageRepository imageRepository;

    @Autowired private FakeCartService cartService;

    @Autowired private ReviewService reviewService;

    User user;

    @BeforeEach
    public void setUser() {
        UserRegister userRegister = new UserRegister("tkddnr@naver.com",
                "상욱", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, new ArrayList<>());
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
        AddItem newAddItem = new AddItem();
        newAddItem.setItemName("장롱");
        newAddItem.setItemCount(5);
        newAddItem.setItemInfo("최고의 장롱");
        newAddItem.setCategory(Category.FURNITURE);
        newAddItem.setPrice(2_000_000);
        List<String> newImage = List.of("imageLink4", "imageLink5", "imageLink6");
        newAddItem.setImage(newImage);

        item = itemService.update(user, item.getId(), newAddItem);

        //then
        assertThat(item.getId()).isNotNull();
        assertThat(item.getUser()).isEqualTo(user);
        assertThat(item.getCategory()).isEqualTo(Category.FURNITURE);
        assertThat(item.getPrice()).isEqualTo(2_000_000);
        assertThat(item.getName()).isEqualTo("장롱");
        assertThat(item.getItemCount()).isEqualTo(5);
        assertThat(item.getItemInfo()).isEqualTo("최고의 장롱");

        List<ItemImage> images = item.getImages();
        assertThat(images).size().isEqualTo(3);

        List<String> imageLinks = new ArrayList<>();
        for (ItemImage itemImage : images) {
            imageLinks.add(itemImage.getImageLink());
        }

        assertThat(imageLinks.contains("imageLink4")).isTrue();
        assertThat(imageLinks.contains("imageLink5")).isTrue();
        assertThat(imageLinks.contains("imageLink6")).isTrue();
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
                "01011112222", "2012-11-22",
                Gender.MALE, new ArrayList<>());
        User user2 = userService.register(userRegister);

        //when

        //then
        assertThatThrownBy(() -> itemService.delete(user2, item.getId())).isInstanceOf(UserValidationException.class);
    }

    @Test
    @DisplayName("사용자는 상품목록을 확인할 수 있다1 - 값 확인")
    void test4_1() {
        //given
        AddItem addItem = new AddItem();
        addItem.setItemName("장롱");
        addItem.setItemCount(3);
        addItem.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem.setCategory(Category.FURNITURE);
        addItem.setPrice(1_000_000);
        List<String> image = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem.setImage(image);
        Item add = itemService.add(user, addItem);

        //when
        Page<ItemList> list = itemService.getList(1, "latest", null, null);

        //then
        List<ItemList> itemList = list.getContent();
        ItemList item = itemList.get(0);

        assertThat(item.getItemId()).isEqualTo(add.getId());
        assertThat(item.getItemName()).isEqualTo(add.getName());
        assertThat(item.getPrice()).isEqualTo(add.getPrice());
        assertThat(item.getCategory()).isEqualTo(add.getCategory());
        assertThat(item.getItemCount()).isEqualTo(add.getItemCount());
        assertThat(item.getUploadUserId()).isEqualTo(add.getUser().getId());
        assertThat(item.getUploadUserName()).isEqualTo(add.getUser().getName());
        assertThat(item.getTitleImage()).isEqualTo("imageLink1");
    }

    @Test
    @DisplayName("사용자는 상품목록을 확인할 수 있다2 - parameter 인자 확인(sortType : latest)")
    void test4_2() {
        //given
        AddItem addItem = new AddItem();
        addItem.setItemName("장롱");
        addItem.setItemCount(3);
        addItem.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem.setCategory(Category.FURNITURE);
        addItem.setPrice(1_000_000);
        List<String> image = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem.setImage(image);
        Item add1 = itemService.add(user, addItem);

        AddItem newAddItem = new AddItem();
        newAddItem.setItemName("장롱");
        newAddItem.setItemCount(5);
        newAddItem.setItemInfo("최고의 장롱");
        newAddItem.setCategory(Category.FURNITURE);
        newAddItem.setPrice(2_000_000);
        List<String> newImage = List.of("imageLink4", "imageLink5", "imageLink6");
        newAddItem.setImage(newImage);
        Item add2 = itemService.add(user, newAddItem);

        //when
        Page<ItemList> list = itemService.getList(1, "latest", null, null);

        //then
        List<ItemList> itemList = list.getContent();

        // sortType을 latest로 주었으므로 첫번째 항목이 add2, 두번째 항목이 add1이 되어야 한다
        ItemList itemList1 = itemList.get(0);
        ItemList itemList2 = itemList.get(1);

        assertThat(itemList1.getItemId()).isEqualTo(add2.getId());
        assertThat(itemList2.getItemId()).isEqualTo(add1.getId());
    }

    @Test
    @DisplayName("사용자는 상품목록을 확인할 수 있다3 - parameter 인자 확인(sortType : reviewCount)")
    void test4_3() {
        //given
        AddItem addItem = new AddItem();
        addItem.setItemName("장롱");
        addItem.setItemCount(3);
        addItem.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem.setCategory(Category.FURNITURE);
        addItem.setPrice(1_000_000);
        List<String> image = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem.setImage(image);
        Item add1 = itemService.add(user, addItem);

        AddItem newAddItem = new AddItem();
        newAddItem.setItemName("장롱");
        newAddItem.setItemCount(5);
        newAddItem.setItemInfo("최고의 장롱");
        newAddItem.setCategory(Category.FURNITURE);
        newAddItem.setPrice(2_000_000);
        List<String> newImage = List.of("imageLink4", "imageLink5", "imageLink6");
        newAddItem.setImage(newImage);
        Item add2 = itemService.add(user, newAddItem);

        UserRegister userRegister = new UserRegister("aaaa@naver.com",
                "son", "123123",
                "01011112222", "2024-11-22",
                Gender.FEMALE, new ArrayList<>());
        User newUser = userService.register(userRegister);

        //새로운 유저로 구매시킨 후 review 작성
        AddCart addCart = new AddCart();
        addCart.setItemId(add1.getId());
        addCart.setItemCount(2);

        cartService.add(newUser.getId(), addCart);
        cartService.order(newUser.getId());
        ReviewWrite reviewWrite = new ReviewWrite();
        reviewWrite.setContent("굿");
        reviewWrite.setScore(5.0f);
        reviewService.reviewWrite(newUser.getId(), add1.getId(), reviewWrite);


        //when
        Page<ItemList> list = itemService.getList(1, "reviewCount", null, null);

        //then
        List<ItemList> itemList = list.getContent();

        ItemList itemList1 = itemList.get(0);
        ItemList itemList2 = itemList.get(1);

        //add1에 review를 주었으므로 reviewCount 순으로 검색하면 add1이 상위에 조회된다
        assertThat(itemList1.getItemId()).isEqualTo(add1.getId());
        assertThat(itemList2.getItemId()).isEqualTo(add2.getId());
    }

    @Test
    @DisplayName("사용자는 상품목록을 확인할 수 있다4 - parameter 인자 확인(sortType : reviewAverage")
    void test4_4() {
        //given
        AddItem addItem = new AddItem();
        addItem.setItemName("장롱");
        addItem.setItemCount(3);
        addItem.setItemInfo("장롱입니다 매우 상태가 좋습니다");
        addItem.setCategory(Category.FURNITURE);
        addItem.setPrice(1_000_000);
        List<String> image = List.of("imageLink1", "imageLink2", "imageLink3");
        addItem.setImage(image);
        Item add1 = itemService.add(user, addItem);

        AddItem newAddItem = new AddItem();
        newAddItem.setItemName("장롱");
        newAddItem.setItemCount(5);
        newAddItem.setItemInfo("최고의 장롱");
        newAddItem.setCategory(Category.FURNITURE);
        newAddItem.setPrice(2_000_000);
        List<String> newImage = List.of("imageLink4", "imageLink5", "imageLink6");
        newAddItem.setImage(newImage);
        Item add2 = itemService.add(user, newAddItem);

        UserRegister userRegister = new UserRegister("aaaa@naver.com",
                "son", "123123",
                "01011112222", "2024-11-22",
                Gender.FEMALE, new ArrayList<>());
        User newUser = userService.register(userRegister);

        //새로운 유저로 2개의 상품 모두 구매시킨 후 review 점수를 다르게 작성
        AddCart addCart1 = new AddCart();
        addCart1.setItemId(add1.getId());
        addCart1.setItemCount(2);

        AddCart addCart2 = new AddCart();
        addCart2.setItemId(add2.getId());
        addCart2.setItemCount(2);

        cartService.add(newUser.getId(), addCart1);
        cartService.add(newUser.getId(), addCart2);
        cartService.order(newUser.getId());

        ReviewWrite reviewWrite1 = new ReviewWrite();
        reviewWrite1.setContent("별로에요");
        reviewWrite1.setScore(3.0f);

        ReviewWrite reviewWrite2 = new ReviewWrite();
        reviewWrite2.setContent("굿");
        reviewWrite2.setScore(5.0f);

        reviewService.reviewWrite(newUser.getId(), add1.getId(), reviewWrite1);
        reviewService.reviewWrite(newUser.getId(), add2.getId(), reviewWrite2);


        //when
        Page<ItemList> list = itemService.getList(1, "reviewAverage", null, null);

        //then
        List<ItemList> itemList = list.getContent();

        ItemList itemList1 = itemList.get(0);
        ItemList itemList2 = itemList.get(1);

        //add1에 3점, add2에 5점 주었으므로 add2가 상위에 조회된다
        assertThat(itemList1.getItemId()).isEqualTo(add2.getId());
        assertThat(itemList2.getItemId()).isEqualTo(add1.getId());
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
