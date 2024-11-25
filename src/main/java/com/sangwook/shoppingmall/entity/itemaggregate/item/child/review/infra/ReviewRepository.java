package com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.infra;

import com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto.MyReview;
import com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto.ReviewList;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByItemIdAndUserId(Long itemId, Long userId);

    @Query("select new com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto.ReviewList(r) from Review r join fetch r.user where r.item.id =:itemId order by r.time desc")
    Page<ReviewList> getList(@Param("itemId") Long itemId, Pageable pageable);

    @Query("select new com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto.MyReview(r) from Review r join fetch r.item where r.user.id=:userId order by r.time desc")
    List<MyReview> findMyReviews(Long userId);
}
