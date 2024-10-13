package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Review;
import com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto.ReviewList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select new com.sangwook.shoppingmall.entity.review.dto.ReviewList(r) from Review r join fetch r.user where r.item.id =:itemId order by r.time desc")
    Page<ReviewList> getList(@Param("itemId") Long itemId, Pageable pageable);
}
