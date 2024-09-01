package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.member.Member;
import com.sangwook.shoppingmall.domain.purchase.Purchase;
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

    public void add(AddItem addItem, Member member) {
        Item item = Item.add(addItem, member);
        itemRepository.save(item);
    }

    public Page<Item> getRandomItem(Pageable pageable) {
        return itemRepository.findItemRandom(pageable);
    }

    public Item findItemById(Long itemId) {
        return itemRepository.findById(itemId).get();
    }

    public void purchase(Long itemId, Member member, Integer count) {
        Item item = itemRepository.findById(itemId).get();

        int totalPrice = count * item.getPrice();
        Purchase purchase = new Purchase(member, item, item.getCategory(), count, totalPrice);
        purchaseRepository.save(purchase);

        item.minusCount(count);
    }
}
