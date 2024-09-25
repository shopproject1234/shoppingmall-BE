package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.item.dto.ItemInfo;
import com.sangwook.shoppingmall.domain.item.dto.ItemList;
import com.sangwook.shoppingmall.domain.itemImage.ItemImage;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.repository.ImageRepository;
import com.sangwook.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public void delete(User user, Long itemId) {
        Item item = getItem(itemId);
        if (checkMine(user, item)) {
            itemRepository.delete(item);
            imageRepository.deleteAllByItemId(itemId);
            return;
        }
        throw new IllegalStateException(); //FIXME
    }

    public ItemInfo getInfo(Long itemId) {
        Item item = getItem(itemId);
        List<ItemImage> image = imageRepository.findByItemId(itemId);

        return new ItemInfo(item, item.getUser(), image);
    }

    public Page<ItemList> getList(int page, String sortType, String keyword, String category) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        return itemRepository.findAllBySortType(sortType, keyword, category, pageRequest);
    }

    private Item getItem(Long itemId) {
        return itemRepository.findById(itemId).get();
    }

    private Boolean checkMine(User user, Item item) {
        return item.getUser().equals(user);
    }
}
