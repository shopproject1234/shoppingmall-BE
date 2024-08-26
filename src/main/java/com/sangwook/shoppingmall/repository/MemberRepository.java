package com.sangwook.shoppingmall.repository;

import com.sangwook.shoppingmall.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
