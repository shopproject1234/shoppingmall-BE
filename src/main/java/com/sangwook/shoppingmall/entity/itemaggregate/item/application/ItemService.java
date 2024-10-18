package com.sangwook.shoppingmall.entity.itemaggregate.item.application;

import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.AddItem;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.ItemInfo;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.ItemImage;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.ItemList;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.exception.custom.ObjectNotFoundException;
import com.sangwook.shoppingmall.exception.custom.UserValidationException;
import com.sangwook.shoppingmall.entity.itemaggregate.item.child.itemImage.infra.ImageRepository;
import com.sangwook.shoppingmall.entity.itemaggregate.item.infra.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;

    public Item add(User user, AddItem addItem) {
        Item item = Item.add(addItem, user);
        item.addImage(addItem.getImage());
        itemRepository.save(item);
        return item;
    }

    public void delete(User user, Long itemId) {
        Item item = getItem(itemId);
        checkMine(user, item);
        itemRepository.delete(item);
    }

    public Item update(User user, Long itemId, AddItem addItem) {
        Item item = getItem(itemId);
        checkMine(user, item);
        item = item.update(addItem);
        return itemRepository.save(item);
    }

    /**
     * 조회만을 위해 repository에서 직접 하는 것은 DDD의 개념에 위배되지 않음
     */
    public ItemInfo getInfo(Long itemId) {
        Item item = getItem(itemId);
        List<ItemImage> image = imageRepository.findByItemId(itemId);
        return new ItemInfo(item, image);
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
