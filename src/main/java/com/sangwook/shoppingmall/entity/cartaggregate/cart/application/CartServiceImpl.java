package com.sangwook.shoppingmall.entity.cartaggregate.cart.application;

import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.Cart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.AddCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.DeleteCart;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.domain.dto.MyCart;
import com.sangwook.shoppingmall.entity.historyaggregate.history.domain.History;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.exception.custom.MyItemException;
import com.sangwook.shoppingmall.exception.custom.ObjectNotFoundException;
import com.sangwook.shoppingmall.entity.cartaggregate.cart.infra.CartRepository;
import com.sangwook.shoppingmall.entity.historyaggregate.history.infra.HistoryRepository;
import com.sangwook.shoppingmall.entity.itemaggregate.item.infra.ItemRepository;
import com.sangwook.shoppingmall.entity.useraggregate.user.infra.UserRepository;
import com.sangwook.shoppingmall.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

@Transactional
@Service
@RequiredArgsConstructor
@Primary
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    private final EmailService emailService;

    //장바구니 상품 등록
    @Override
    public Cart add(Long userId, AddCart addCart) {
        User user = getUser(userId);
        Item item = getItem(addCart.getItemId());

        if (user.equals(item.getUser())) {
            throw new MyItemException("본인의 상품은 카트에 추가할 수 없습니다", getMethodName());
        }

        Optional<Cart> cart = getCart(userId, addCart.getItemId());
        if (cart.isPresent()) { //이미 장바구니에 해당 상품이 존재할 경우 예외 처리
            throw new IllegalStateException(); //FIXME
        }

        //카트에 담으려는 상품의 수보다 남은 상품의 재고가 적은 경우
        if (item.getItemCount() < addCart.getItemCount()) {
            throw new IllegalStateException(); //FIXME
        }
        Cart newCart = Cart.add(user, item, addCart.getItemCount());
        return cartRepository.save(newCart);
    }

    @Override
    public void delete(Long userId, DeleteCart deleteCart) {
        Optional<Cart> cart = getCart(userId, deleteCart.getItemId());
        cart.ifPresent(cartRepository::delete);
    }

    /**
     * MyCart에서 Cart 객체의 Item 정보를 가져오면서 Item에 대한 N+1문제 발생하여
     * Item객체를 join fetch로 가져와 문제를 해결
     */
    @Override
    public List<MyCart> getMyCart(Long userId) {
        List<Cart> carts = cartRepository.findAllByUserIdFetchItem(userId);
        List<MyCart> myCart = new ArrayList<>();
        for (Cart cart : carts) {
            myCart.add(new MyCart(cart));
        }
        return myCart;
    }

    @Override
    public void order(Long userId) { //FIXME order메서드에서 주문시 item의 수량 줄여야함 N+1 문제 조심
        List<Cart> carts = cartRepository.findAllByUserId(userId);
        for (Cart cart : carts) {
            //주문 시에도 상품의 수를 재확인, 주문하려는 수량보다 재고가 적게 남은 경우 예외처리
            //구매 성공시 상품의 수량을 줄인다
            Item item = cart.getItem(); //카트에 담긴 아이템 만큼 N+1 문제 가능성
            if (item.getItemCount() < cart.getCount()) {
                throw new IllegalStateException(); //FIXME
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
