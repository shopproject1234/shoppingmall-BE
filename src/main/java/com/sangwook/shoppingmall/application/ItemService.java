package com.sangwook.shoppingmall.application;

import com.sangwook.shoppingmall.common.constant.Category;
import com.sangwook.shoppingmall.entity.itemaggregate.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.AddItem;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.ItemInfo;
import com.sangwook.shoppingmall.entity.itemaggregate.domain.ItemImage;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.ItemList;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.interest.infra.InterestRepository;
import com.sangwook.shoppingmall.entity.useraggregate.user.infra.UserRepository;
import com.sangwook.shoppingmall.exception.custom.ObjectNotFoundException;
import com.sangwook.shoppingmall.exception.custom.UserValidationException;
import com.sangwook.shoppingmall.entity.itemaggregate.itemImage.infra.ImageRepository;
import com.sangwook.shoppingmall.entity.itemaggregate.item.infra.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.sangwook.shoppingmall.constant.Scale.INFO;
import static com.sangwook.shoppingmall.constant.Scale.SEARCH;
import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ItemService {

    private final ItemRepository itemRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @CacheEvict(value = "itemListCache", allEntries = true)
    public Item add(User user, AddItem addItem) {
        Item item = Item.add(addItem, user);
        item.addImage(addItem.getImage());
        itemRepository.save(item);
        return item;
    }

    @CacheEvict(value = "itemListCache", allEntries = true)
    public void delete(User user, Long itemId) {
        Item item = getItem(itemId);
        checkMine(user, item);
        itemRepository.delete(item);
    }

    @CacheEvict(value = "itemListCache", allEntries = true)
    public Item update(User user, Long itemId, AddItem addItem) {
        Item item = getItem(itemId);
        checkMine(user, item);
        item = item.update(addItem);
        return itemRepository.save(item);
    }

    /**
     * 조회만을 위해 repository에서 직접 하는 것은 DDD의 개념에 위배되지 않음
     */
    public ItemInfo getInfo(Long itemId, User user) {
        Item item = getItem(itemId);
        if (user != null) {
            User getUser = getUserFetchInterest(user.getId());
            getUser.plusScale(item.getCategory(), INFO.getValue());
        }
        List<ItemImage> image = imageRepository.findByItemId(itemId);
        return new ItemInfo(item, image);
    }

    @Cacheable(value = "itemListCache", key = "#page", //페이지별 캐싱
            condition = "#sortType == 'latest' && #keyword == null && #category == null")
    public Page<ItemList> getList(int page, String sortType, String keyword, String category, User user) {
        //키워드 검색 시 가중치에 반영
        if (keyword != null && user != null) {
            User getUser = getUserFetchInterest(user.getId());
            Optional<Category> mostFrequentCategory = itemRepository.findMostFrequentCategory(keyword);
            mostFrequentCategory.ifPresent(value -> getUser.plusScale(value, SEARCH.getValue()));
        }
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

    private User getUserFetchInterest(Long userId) {
        Optional<User> user = userRepository.findUserFetchInterest(userId);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException(getMethodName());
        }
        return user.get();
    }
}
