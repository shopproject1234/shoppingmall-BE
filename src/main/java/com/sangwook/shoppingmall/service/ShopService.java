package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.domain.history.History;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.repository.ItemRepository;
import com.sangwook.shoppingmall.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {

    private final ItemRepository itemRepository;
    private final PurchaseRepository purchaseRepository;

    public void add(AddItem addItem, User user) {
        Item item = Item.add(addItem, user);
        itemRepository.save(item);
    }

    public Page<Item> getRandomItem(Pageable pageable) {
        return itemRepository.findItemRandom(pageable);
    }

    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId).get();
    }

    public void purchase(Long itemId, User user, Integer count) {
        Item item = itemRepository.findById(itemId).get();

        int totalPrice = count * item.getPrice();
        History history = new History(user, item, item.getCategory(), count, totalPrice);
        purchaseRepository.save(history);

        item.purchased(count);
    }

    public void deleteItem(Long itemId) {
        Item item = itemRepository.findById(itemId).get();
        itemRepository.delete(item);
    }

    public void editItem(Long itemId) {

    }
}
