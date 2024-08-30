package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.domain.item.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(
            value = "select * from Item order by RAND() \n-- #pageable\n",
            countQuery = "SELECT COUNT(*) FROM Item",
            nativeQuery = true
    )
    Page<Item> findItemRandom(Pageable pageable);
}
