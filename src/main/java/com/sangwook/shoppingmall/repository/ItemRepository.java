package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long>, CustomItemRepository{

}
