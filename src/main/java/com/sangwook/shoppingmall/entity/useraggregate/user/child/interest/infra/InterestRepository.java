package com.sangwook.shoppingmall.entity.useraggregate.user.child.interest.infra;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    Optional<Interest> findByUserIdAndCategory(Long userId, Category category);

    @Query("select i from Interest i where i.user.id=:userId")
    List<Interest> findAllById(Long userId);

}
