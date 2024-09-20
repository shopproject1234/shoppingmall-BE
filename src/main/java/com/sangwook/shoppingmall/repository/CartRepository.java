package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.domain.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("select c from Cart c where c.item.id=:itemId and c.user.id=:userId")
    Optional<Cart> findByItemId(@Param("itemId") Long itemId, @Param("userId") Long userId);
}
