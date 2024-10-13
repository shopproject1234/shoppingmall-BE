package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.ItemList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomItemRepository {

    Page<ItemList> findAllBySortType(String sortType, String keyword, String category, Pageable pageable);
}
