package com.sangwook.shoppingmall.entity.itemaggregate.item.infra;

import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.MyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, CustomItemRepository {

    @Query("select i from Item i join fetch i.reviews where i.id=:itemId")
    Optional<Item> findItemFetchReview(Long itemId);

    @Query("select new com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.MyItem(i) from Item i where i.user.id =:userId order by i.time desc")
    List<MyItem> findMyItems(Long userId);
}
