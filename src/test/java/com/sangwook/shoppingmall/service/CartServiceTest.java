package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.Cart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.AddCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.DeleteCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.MyCart;
import com.sangwook.shoppingmall.entity.historyaggregate.history.domain.History;
import com.sangwook.shoppingmall.entity.itemaggregate.item.application.ItemService;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.AddItem;
import com.sangwook.shoppingmall.entity.useraggregate.user.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.infra.CartRepository;
import com.sangwook.shoppingmall.entity.historyaggregate.history.infra.HistoryRepository;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.dto.UserRegister;
import com.sangwook.shoppingmall.exception.custom.MyItemException;
import com.sangwook.shoppingmall.exception.custom.QuantityNotEnoughException;
import com.sangwook.shoppingmall.service.fake.FakeCartService;
import com.sangwook.shoppingmall.service.fake.FakeEmailService;
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
public class CartServiceTest {

    @Autowired private UserService userService;
    @Autowired private ItemService itemService;
    @Autowired private FakeCartService cartService; //FakeService 주입 - 해당 Service의 EmailService도 Fake로 구현되어있어 실제 메일이 발송되지 않는다
    @Autowired private CartRepository cartRepository;
    @Autowired private HistoryRepository historyRepository;
    @Autowired private FakeEmailService fakeEmailService;

    User user;
    Item item;

    @BeforeEach
    public void set() {
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
    }

    @Test
    @DisplayName("사용자는 원하는 상품을 장바구니에 저장할 수 있다")
    void test1_1() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User user = userService.register(userRegister);

        //when
        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        Cart cart = cartService.add(user.getId(), addCart);

        //then
        assertThat(cart.getItem()).isEqualTo(item);
        assertThat(cart.getCount()).isEqualTo(3);
        assertThat(cart.getPrice()).isEqualTo(item.getPrice());
        assertThat(cart.getCategory()).isEqualTo(item.getCategory());
    }

    @Test
    @DisplayName("사용자는 이미 담긴 상품을 장바구니에 또 담을 경우 예외 처리한다")
    void test1_2() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User user = userService.register(userRegister);

        //when
        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        cartService.add(user.getId(), addCart);

        //then
        assertThatThrownBy(() -> cartService.add(user.getId(), addCart)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("사용자는 자신이 올린 상품을 장바구니에 담을 수 없다")
    void test1_3() {
        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);

        assertThatThrownBy(() -> cartService.add(user.getId(), addCart)).isInstanceOf(MyItemException.class);
    }

    @Test
    @DisplayName("카트에 담으려는 상품의 수보다 남은 상품의 재고가 적은 경우 상품을 담을 수 없다")
    void test1_4() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User user = userService.register(userRegister);

        //when
        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(4); //재고가 3인 상품을 4개나 담으려고 하는 경우
        assertThatThrownBy(() -> cartService.add(user.getId(), addCart)).isInstanceOf(QuantityNotEnoughException.class);
    }

    @Test
    @DisplayName("장바구니에 등록된 상품을 삭제할 수 있다")
    void test2() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User user = userService.register(userRegister);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        Cart cart = cartService.add(user.getId(), addCart);

        //when
        DeleteCart deleteCart = new DeleteCart();
        deleteCart.setItemId(item.getId());

        cartService.delete(user.getId(), deleteCart);

        //then
        Optional<Cart> getCart = cartRepository.findById(cart.getId());
        assertThat(getCart).isEmpty();

    }

    @Test
    @DisplayName("장바구니에 등록된 상품을 조회할 수 있다")
    void test3() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User user = userService.register(userRegister);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        cartService.add(user.getId(), addCart);

        //when
        List<MyCart> myCart = cartService.getMyCart(user.getId());

        //then
        assertThat(myCart.size()).isEqualTo(1);
        assertThat(myCart.get(0).getItemId()).isEqualTo(item.getId());
        assertThat(myCart.get(0).getItemCount()).isEqualTo(item.getItemCount());
        assertThat(myCart.get(0).getItemName()).isEqualTo(item.getName());
        assertThat(myCart.get(0).getTotalPrice()).isEqualTo(item.getItemCount() * item.getPrice());
    }

    @Test
    @DisplayName("장바구니에 있는 상품들을 최종적으로 주문할 수 있다")
    void test4_1() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User user = userService.register(userRegister);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        cartService.add(user.getId(), addCart);

        //when
        cartService.order(user.getId());

        //then
        assertThat(cartRepository.findAllByUserId(user.getId())).isEmpty();
    }

    @Test
    @DisplayName("장바구니에 있는 상품들을 주문하면 결제내역에 저장된다")
    void test4_2() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User user = userService.register(userRegister);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        cartService.add(user.getId(), addCart);

        Integer price = item.getPrice();
        Integer count = item.getItemCount();

        //when
        cartService.order(user.getId());

        //then
        List<History> history = historyRepository.findAllByUserId(user.getId());
        assertThat(history.size()).isEqualTo(1);
        assertThat(history.get(0).getUser()).isEqualTo(user);
        assertThat(history.get(0).getItem()).isEqualTo(item);
        assertThat(history.get(0).getCategory()).isEqualTo(Category.FURNITURE);
        assertThat(history.get(0).getCount()).isEqualTo(3);
        assertThat(history.get(0).getTotalPrice()).isEqualTo(price * count);
    }

    @Test
    @DisplayName("주문시에 상품의 재고를 확인하여 상품 재고가 주문할 수량보다 적을 경우 예외 처리한다") //잘못된 주문이 들어가는 것을 막기 위함
    void test4_3() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User user = userService.register(userRegister);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        cartService.add(user.getId(), addCart);

        //when
        item.purchased(1); // 주문 전 재고가 1개 줄었다고 가정 - 주문할 수량 : 3, 상품 재고 : 2

        //then
        assertThatThrownBy(() -> cartService.order(user.getId())).isInstanceOf(IllegalStateException.class);

    }

    @Test
    @DisplayName("주문에 성공하여 상품의 재고가 감소한 후 상품의 재고가 3개 이하로 남으면 상품의 주인에게 메일을 발송한다")
    void test4_4() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", "2012-11-22",
                Gender.MALE, List.of());
        User user = userService.register(userRegister);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        cartService.add(user.getId(), addCart);

        cartService.order(user.getId());

        assertThat(fakeEmailService.sended).isTrue();
    }
}
