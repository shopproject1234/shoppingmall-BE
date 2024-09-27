package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.domain.itemImage.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findByItemId(Long itemId);

    void deleteAllByItemId(Long itemId);

    @Query("select i from ItemImage i where i.item.id=:itemId and i.imageNumber = 1")
    ItemImage findTitleImage(Long itemId);
}
