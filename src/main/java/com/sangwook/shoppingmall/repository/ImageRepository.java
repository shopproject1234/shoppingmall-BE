package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.domain.itemImage.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findByItemId(Long itemId);
}
