package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.domain.recommend.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
}
