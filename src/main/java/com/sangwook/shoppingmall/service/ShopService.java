package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.member.Member;
import com.sangwook.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ShopService {

    private final ItemRepository itemRepository;

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
}
