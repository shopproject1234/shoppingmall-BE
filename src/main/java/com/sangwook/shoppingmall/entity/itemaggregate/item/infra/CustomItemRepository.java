package com.sangwook.shoppingmall.entity.itemaggregate.item.infra;

import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.ItemList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomItemRepository {

    Page<ItemList> findAllBySortType(String sortType, String keyword, String category, Pageable pageable);
}
