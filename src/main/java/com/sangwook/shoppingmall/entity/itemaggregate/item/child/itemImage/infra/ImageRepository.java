package com.sangwook.shoppingmall.entity.itemaggregate.item.child.itemImage.infra;

import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ItemImage, Long> {
    List<ItemImage> findByItemId(Long itemId);
}
