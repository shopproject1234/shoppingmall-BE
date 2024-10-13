package com.sangwook.shoppingmall.entity.itemaggregate.item.application;

import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.AddItem;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.ItemInfo;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.ItemList;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.ItemImage;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.exception.custom.ObjectNotFoundException;
import com.sangwook.shoppingmall.exception.custom.UserValidationException;
import com.sangwook.shoppingmall.repository.ImageRepository;
import com.sangwook.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

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
        checkMine(user, item);
        itemRepository.delete(item);
        imageRepository.deleteAllByItemId(itemId);
    }

    public ItemInfo getInfo(Long itemId) {
        Item item = getItem(itemId);
        List<ItemImage> image = imageRepository.findByItemId(itemId);

        return new ItemInfo(item, item.getUser(), image);
    }

    public Page<ItemList> getList(int page, String sortType, String keyword, String category) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        return itemRepository.findAllBySortType(sortType, keyword, category, pageRequest);
    }

    private Item getItem(Long itemId) {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new ObjectNotFoundException(getMethodName());
        }
        return item.get();
    }

    private void checkMine(User user, Item item) {
        if (!item.getUser().equals(user)) {
            throw new UserValidationException(getMethodName());
        }
    }
}
