package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.domain.cart.Cart;
import com.sangwook.shoppingmall.domain.cart.dto.AddCart;
import com.sangwook.shoppingmall.domain.cart.dto.DeleteCart;
import com.sangwook.shoppingmall.domain.cart.dto.MyCart;
import com.sangwook.shoppingmall.domain.history.History;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.user.User;
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
            throw new IllegalStateException(); //FIXME
        }

        Optional<Cart> cart = getCart(userId, addCart.getItemId());
        if (cart.isPresent()) { //이미 장바구니에 해당 상품이 존재할 경우 카운트를 늘려줌
            Cart getCart = cart.get();
            getCart.addCount(addCart.getItemCount());
            return getCart;
        }
        Cart newCart = Cart.add(user, item, addCart.getItemCount());
        return cartRepository.save(newCart);
    }

    public void delete(Long userId, DeleteCart deleteCart) {
        Optional<Cart> cart = getCart(userId, deleteCart.getItemId());
        cart.ifPresent(cartRepository::delete);
    }

    public List<MyCart> getMyCart(Long userId) {
        List<Cart> carts = cartRepository.findAllByUserId(userId);
        List<MyCart> myCart = new ArrayList<>();
        for (Cart cart : carts) {
            myCart.add(new MyCart(cart));
        }
        return myCart;
    }

    public void order(Long userId) {
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
        return userRepository.findById(userId).get();
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId).get();
    }

    private Optional<Cart> getCart(Long userId, Long itemId) {
        return cartRepository.findByItemId(itemId, userId);
    }
}
