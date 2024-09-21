package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.domain.history.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByUserId(Long userId);
}
