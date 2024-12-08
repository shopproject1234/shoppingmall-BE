package com.sangwook.shoppingmall.service.fake;

import com.sangwook.shoppingmall.entity.cartaggregate.cart.application.CartService;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.Cart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.AddCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.DeleteCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.MyCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.infra.CartRepository;
import com.sangwook.shoppingmall.entity.historyaggregate.history.domain.History;
import com.sangwook.shoppingmall.entity.historyaggregate.history.infra.HistoryRepository;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.infra.ItemRepository;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.infra.UserRepository;
import com.sangwook.shoppingmall.exception.custom.MyItemException;
import com.sangwook.shoppingmall.exception.custom.ObjectNotFoundException;
import com.sangwook.shoppingmall.exception.custom.QuantityNotEnoughException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

@Transactional
@Service
public class FakeCartService implements CartService {

    @Autowired
    CartRepository cartRepository;
    @Autowired ItemRepository itemRepository;
    @Autowired UserRepository userRepository;
    @Autowired HistoryRepository historyRepository;
    @Autowired FakeEmailService emailService;

    @Override
    public Cart add(Long userId, AddCart addCart) {
        User user = getUser(userId);
        Item item = getItem(addCart.getItemId());

        user.plusScale(item.getCategory(), 5);

        if (user.equals(item.getUser())) {
            throw new MyItemException("본인의 상품은 카트에 추가할 수 없습니다", getMethodName());
        }

        Optional<Cart> cart = getCart(userId, addCart.getItemId());
        if (cart.isPresent()) {
            throw new IllegalStateException();
        }

        //카트에 담으려는 상품의 수보다 남은 상품의 재고가 적은 경우
        if (item.getItemCount() < addCart.getItemCount()) {
            throw new QuantityNotEnoughException("상품의 재고가 부족합니다");
        }
        Cart newCart = Cart.add(user, item, addCart.getItemCount());
        return cartRepository.save(newCart);
    }

    @Override
    public void delete(Long userId, DeleteCart deleteCart) {
        Optional<Cart> cart = getCart(userId, deleteCart.getItemId());
        cart.ifPresent(cartRepository::delete);
    }

    @Override
    public List<MyCart> getMyCart(Long userId) {
        List<Cart> carts = cartRepository.findAllByUserIdFetchItem(userId);
        List<MyCart> myCart = new ArrayList<>();
        for (Cart cart : carts) {
            myCart.add(new MyCart(cart));
        }
        return myCart;
    }

    /**
     * MyCart에서 Cart 객체의 Item 정보를 가져오면서 Item에 대한 N+1문제 발생
     * getMyCart의 기존 코드였고, Test코드에서만 사용한다
     */
    @Deprecated
    public List<MyCart> getMyCartNPlusOne(Long userId) {
        List<Cart> carts = cartRepository.findAllByUserId(userId);
        List<MyCart> myCart = new ArrayList<>();
        for (Cart cart : carts) {
            myCart.add(new MyCart(cart));
        }
        return myCart;
    }

    @Override
    public void order(Long userId) {
        List<Cart> carts = cartRepository.findAllByUserIdFetchItem(userId);
        if (carts.isEmpty()) {
            throw new ObjectNotFoundException("장바구니에 상품이 없습니다", getMethodName());
        }
        for (Cart cart : carts) {
            //주문 시에도 상품의 수를 재확인, 주문하려는 수량보다 재고가 적게 남은 경우 예외처리
            //구매 성공시 상품의 수량을 줄인다
            Item item = cart.getItem(); //카트에 담긴 아이템 만큼 N+1 문제 가능성
            if (item.getItemCount() < cart.getCount()) {
                throw new IllegalStateException();
            }
            item.purchased(cart.getCount());
            History history = History.purchased(cart);
            if (item.getItemCount() <= 3) {
                emailService.sendItemMail(item);
            }
            historyRepository.save(history);
        }
        cartRepository.deleteAll(carts);
    }

    /**
     * Cart에서 getItem을 하면서 N + 1발생
     * order의 기존코드였고, Test코드에서만 사용한다
     */
    @Deprecated
    public void orderNPlusOne(Long userId) {
        List<Cart> carts = cartRepository.findAllByUserId(userId);
        for (Cart cart : carts) {
            //주문 시에도 상품의 수를 재확인, 주문하려는 수량보다 재고가 적게 남은 경우 예외처리
            //구매 성공시 상품의 수량을 줄인다
            Item item = cart.getItem(); //카트에 담긴 아이템 만큼 N+1 문제 가능성
            if (item.getItemCount() < cart.getCount()) {
                throw new IllegalStateException();
            }
            History history = History.purchased(cart);
            item.purchased(cart.getCount());
            if (item.getItemCount() <= 3) {
                emailService.sendItemMail(item);
            }
            historyRepository.save(history);
        }
        cartRepository.deleteAll(carts);
    }

    /**
     *  private Method
     *  다른 Aggregate의 Root Entity만 private Method로 조회하여 사용
     */
    private User getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException(getMethodName());
        }
        return user.get();
    }

    private Item getItem(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new ObjectNotFoundException(getMethodName());
        }
        return item.get();
    }

    private Optional<Cart> getCart(Long userId, Long itemId) {
        return cartRepository.findByItemId(itemId, userId);
    }
}
