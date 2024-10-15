package com.sangwook.shoppingmall.entity.historyaggregate.history.infra;

import com.sangwook.shoppingmall.entity.historyaggregate.history.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByUserId(Long userId);

    Optional<History> findByUserIdAndItemId(Long userId, Long itemId);
}
