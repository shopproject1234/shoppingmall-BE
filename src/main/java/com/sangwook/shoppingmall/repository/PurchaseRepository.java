package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.entity.historyaggregate.history.infra.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<History, Long> {
}
