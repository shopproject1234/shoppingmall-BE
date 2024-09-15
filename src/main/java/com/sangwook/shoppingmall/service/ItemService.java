package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.itemImage.ItemImage;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.repository.ImageRepository;
import com.sangwook.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;

    public Item add(User user, AddItem addItem) {
        Item item = Item.add(addItem, user);
        item = itemRepository.save(item);

        List<String> image = addItem.getImage();
        List<ItemImage> images = ItemImage.addImage(image, item);
        imageRepository.saveAll(images);
        return item;
    }
}
