package com.sangwook.shoppingmall.entity.cartaggregate.cart.infra;

import com.sangwook.shoppingmall.entity.cartaggregate.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c where c.item.id=:itemId and c.user.id=:userId")
    Optional<Cart> findByItemId(@Param("itemId") Long itemId, @Param("userId") Long userId);

    List<Cart> findAllByUserId(Long userId);

    @Query("select c from Cart c join fetch c.item where c.user.id=:userId")
    List<Cart> findAllByUserIdFetchItem(Long userId);
}
