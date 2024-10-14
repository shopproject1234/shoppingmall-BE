package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, CustomItemRepository{

    @Query("select i from Item i join fetch ItemImage ii on ii.item.id = i.id where i.id=:itemId")
    Optional<Item> findItemFetchImage(Long itemId);
}
