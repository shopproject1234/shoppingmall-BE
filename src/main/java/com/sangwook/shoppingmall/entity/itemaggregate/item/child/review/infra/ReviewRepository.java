package com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.infra;

import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
