package com.sangwook.shoppingmall.entity.itemaggregate.itemImage.infra;

import com.sangwook.shoppingmall.entity.itemaggregate.domain.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ItemImage, Long> {
    List<ItemImage> findByItemId(Long itemId);
}
