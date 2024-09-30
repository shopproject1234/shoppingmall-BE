package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
