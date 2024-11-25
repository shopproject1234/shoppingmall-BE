package com.sangwook.shoppingmall.entity.historyaggregate.history.infra;

import com.sangwook.shoppingmall.entity.historyaggregate.history.domain.History;
import com.sangwook.shoppingmall.entity.historyaggregate.history.domain.dto.MyPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllByUserId(Long userId);

    Optional<History> findByUserIdAndItemId(Long userId, Long itemId);

    @Query("select new com.sangwook.shoppingmall.entity.historyaggregate.history.domain.dto.MyPurchase(h) from History h join fetch h.item where h.user.id=:userId order by h.date desc")
    List<MyPurchase> findMyPurchases(Long userId);
}
