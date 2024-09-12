package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.user.dto.UserLogin;
import com.sangwook.shoppingmall.domain.user.dto.UserRegister;
import com.sangwook.shoppingmall.repository.UserRepository;
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
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User register(UserRegister userRegister) {
        Optional<User> getMember = userRepository.findByEmail(userRegister.getEmail());
        if (getMember.isPresent()) {
            throw new IllegalStateException();//FIXME
        }
        String encoded = passwordEncoder.encode(userRegister.getPassword());
        User user = User.register(userRegister, encoded);
        return userRepository.save(user);
    }

    public User login(UserLogin userLogin) {
        Optional<User> member = userRepository.findByEmail(userLogin.getEmail());
        if (member.isEmpty() || !passwordEncoder.matches(userLogin.getPassword(), member.get().getPassword())) {
            throw new IllegalStateException();
        }
        return member.get();
    }

    private User getUserByEmail(String email) {
        Optional<User> getUser = userRepository.findByEmail(email);
        if (getUser.isEmpty()) {
            throw new IllegalStateException(); //FIXME
        }
        return getUser.get();
    }


}
