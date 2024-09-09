package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.user.dto.MemberLogin;
import com.sangwook.shoppingmall.domain.user.dto.MemberRegister;
import com.sangwook.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void register(MemberRegister memberRegister) {

        Optional<User> getMember = memberRepository.findByEmail(memberRegister.getEmail());
        if (getMember.isPresent()) {
            //TODO 이미 아이디가 있으면 view로 돌아가기
        }
        String encoded = passwordEncoder.encode(memberRegister.getPassword());
        User user = User.memberRegister(memberRegister, encoded);
        memberRepository.save(user);
    }

    public User login(MemberLogin memberLogin) {
        Optional<User> member = memberRepository.findByEmail(memberLogin.getEmail());
        if (member.isEmpty() || !passwordEncoder.matches(memberLogin.getPassword(), member.get().getPassword())) {
            throw new IllegalStateException();
        }
        return member.get();
    }


}
