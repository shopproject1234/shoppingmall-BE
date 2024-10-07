package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.domain.cart.Cart;
import com.sangwook.shoppingmall.domain.cart.dto.AddCart;
import com.sangwook.shoppingmall.domain.cart.dto.DeleteCart;
import com.sangwook.shoppingmall.domain.cart.dto.MyCart;
import com.sangwook.shoppingmall.domain.history.History;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.exception.custom.MyItemException;
import com.sangwook.shoppingmall.exception.custom.ObjectNotFoundException;
import com.sangwook.shoppingmall.repository.CartRepository;
import com.sangwook.shoppingmall.repository.HistoryRepository;
import com.sangwook.shoppingmall.repository.ItemRepository;
import com.sangwook.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

@Transactional
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    //장바구니 상품 등록
    public Cart add(Long userId, AddCart addCart) {
        User user = getUser(userId);
        Item item = getItem(addCart.getItemId());

        if (user.equals(item.getUser())) {
            throw new MyItemException("본인의 상품은 카트에 추가할 수 없습니다", getMethodName());
        }

        Optional<Cart> cart = getCart(userId, addCart.getItemId());
        if (cart.isPresent()) { //이미 장바구니에 해당 상품이 존재할 경우 카운트를 늘려줌
            Cart getCart = cart.get();
            getCart.addCount(addCart.getItemCount()); //TODO 해당 상품의 수량 확인 후 원하는 수량보다 적을 경우 Exception 추가 필요
            return getCart;
        }
        Cart newCart = Cart.add(user, item, addCart.getItemCount());
        return cartRepository.save(newCart);
    }

    public void delete(Long userId, DeleteCart deleteCart) {
        Optional<Cart> cart = getCart(userId, deleteCart.getItemId());
        cart.ifPresent(cartRepository::delete);
    }


    public List<MyCart> getMyCart(Long userId) { //TODO N+1 문제 발생 가능성 있음 - 테스트 필요
        List<Cart> carts = cartRepository.findAllByUserId(userId);
        List<MyCart> myCart = new ArrayList<>();
        for (Cart cart : carts) {
            myCart.add(new MyCart(cart));
        }
        return myCart;
    }

    public void order(Long userId) { //FIXME order메서드에서 주문시 item의 수량 줄여야함 N+1 문제 조심
        List<Cart> carts = cartRepository.findAllByUserId(userId);
        for (Cart cart : carts) {
            History history = History.purchased(cart);
            historyRepository.save(history);
        }
        cartRepository.deleteAll(carts);
    }

    /**
     *  private Method
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
