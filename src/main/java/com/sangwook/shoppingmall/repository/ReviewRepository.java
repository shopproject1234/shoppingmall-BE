package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.domain.review.Review;
import com.sangwook.shoppingmall.domain.review.dto.ReviewPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "select new com.sangwook.shoppingmall.domain.review.dto.ReviewPage(r.member.name, r.title, r.content, r.point, r.time) from Review r where r.item.id=:itemId order by r.time desc")
    Page<ReviewPage> findReviewPage(@Param("itemId") Long itemId, Pageable pageable);
}
