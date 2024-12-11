package com.sangwook.shoppingmall.entity.useraggregate.user.infra;

import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.id=:userId")
    Optional<User> findUserFetchInterest(@Param("userId") Long userId);
}
