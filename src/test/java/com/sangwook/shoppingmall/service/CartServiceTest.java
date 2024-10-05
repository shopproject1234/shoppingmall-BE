package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.domain.cart.Cart;
import com.sangwook.shoppingmall.domain.cart.dto.AddCart;
import com.sangwook.shoppingmall.domain.cart.dto.DeleteCart;
import com.sangwook.shoppingmall.domain.cart.dto.MyCart;
import com.sangwook.shoppingmall.domain.history.History;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.user.dto.UserRegister;
import com.sangwook.shoppingmall.exception.custom.MyItemException;
import com.sangwook.shoppingmall.repository.CartRepository;
import com.sangwook.shoppingmall.repository.HistoryRepository;
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
    @Autowired private CartService cartService;
    @Autowired private CartRepository cartRepository;
    @Autowired private HistoryRepository historyRepository;

    User user;
    Item item;

    @BeforeEach
    public void set() {
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
    }

    @Test
    @DisplayName("사용자는 원하는 상품을 장바구니에 저장할 수 있다")
    void test1_1() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", 20121122,
                Gender.MALE);
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
    @DisplayName("사용자는 이미 담긴 상품을 장바구니에 또 담을 경우 카운트가 올라가게 된다")
    void test1_2() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", 20121122,
                Gender.MALE);
        User user = userService.register(userRegister);

        //when
        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        cartService.add(user.getId(), addCart);
        Cart cart = cartService.add(user.getId(), addCart); // 같은 addCart를 2번 추가

        //then
        assertThat(cart.getItem()).isEqualTo(item);
        assertThat(cart.getCount()).isEqualTo(6);
        assertThat(cart.getPrice()).isEqualTo(item.getPrice());
        assertThat(cart.getCategory()).isEqualTo(item.getCategory());
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
    @DisplayName("장바구니에 등록된 상품을 삭제할 수 있다")
    void test2() {
        //given
        UserRegister userRegister = new UserRegister("taaaa@naver.com",
                "상욱2", "123123",
                "01011112222", 20121122,
                Gender.MALE);
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
                "01011112222", 20121122,
                Gender.MALE);
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
                "01011112222", 20121122,
                Gender.MALE);
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
                "01011112222", 20121122,
                Gender.MALE);
        User user = userService.register(userRegister);

        AddCart addCart = new AddCart();
        addCart.setItemId(item.getId());
        addCart.setItemCount(3);
        cartService.add(user.getId(), addCart);

        //when
        cartService.order(user.getId());

        //then
        List<History> history = historyRepository.findAllByUserId(user.getId());
        assertThat(history.size()).isEqualTo(1);
        assertThat(history.get(0).getUser()).isEqualTo(user);
        assertThat(history.get(0).getItem()).isEqualTo(item);
        assertThat(history.get(0).getCategory()).isEqualTo(Category.FURNITURE);
        assertThat(history.get(0).getCount()).isEqualTo(3);
        assertThat(history.get(0).getTotalPrice()).isEqualTo(item.getPrice() * item.getItemCount());
    }
}
