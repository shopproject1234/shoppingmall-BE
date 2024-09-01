package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.domain.purchase.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
}
