package com.sangwook.shoppingmall.entity.itemaggregate.item.infra;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.ItemList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CustomItemRepository {

    Page<ItemList> findAllBySortType(String sortType, String keyword, String category, Pageable pageable);

    Optional<Category> findMostFrequentCategory(String keyword);
}
