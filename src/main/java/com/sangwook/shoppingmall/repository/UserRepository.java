package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
