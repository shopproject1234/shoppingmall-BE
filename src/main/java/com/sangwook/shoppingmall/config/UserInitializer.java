package com.sangwook.shoppingmall.config;

import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.user.dto.UserRegister;
import com.sangwook.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Profile("!test")
public class UserInitializer {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            if (userRepository.findByEmail("tkddnr@naver.com").isEmpty()) {
                UserRegister userRegister = new UserRegister("tkddnr@naver.com", "상욱", "123123", "01011112222", 20120111, Gender.MALE);
                String encoded = passwordEncoder.encode(userRegister.getPassword());

                User user = User.register(userRegister, encoded);
                userRepository.save(user);
            }
        };
    }
}
