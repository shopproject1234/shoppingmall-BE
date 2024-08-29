package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.domain.member.Member;
import com.sangwook.shoppingmall.domain.member.dto.MemberLogin;
import com.sangwook.shoppingmall.domain.member.dto.MemberRegister;
import com.sangwook.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public void register(MemberRegister memberRegister) {

        Optional<Member> getMember = memberRepository.findByEmail(memberRegister.getEmail());
        if (getMember.isPresent()) {
            //TODO 이미 아이디가 있으면 view로 돌아가기
        }
        Member member = Member.memberRegister(memberRegister);
        memberRepository.save(member);
    }

    public void login(MemberLogin memberLogin) {
        Optional<Member> member = memberRepository.findByEmail(memberLogin.getEmail());
        if (member.isEmpty()) {
            log.info("로그인 실패 계정 없음");
            throw new IllegalStateException(); //TODO change exception
        }
        Member getMember = member.get();
        if (!(memberLogin.getPassword()).equals(getMember.getPassword())) {
            log.info("로그인 실패 비밀번호 다름");
            throw new IllegalStateException(); //TODO change exception
        }
    }

}
