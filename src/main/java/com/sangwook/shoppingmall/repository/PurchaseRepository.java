package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.domain.history.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<History, Long> {
}
