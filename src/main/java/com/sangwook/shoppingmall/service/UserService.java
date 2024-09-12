package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.constant.Preference;
import com.sangwook.shoppingmall.domain.interest.Interest;
import com.sangwook.shoppingmall.domain.interest.dto.InterestInfo;
import com.sangwook.shoppingmall.domain.user.User;
import com.sangwook.shoppingmall.domain.user.dto.UserLogin;
import com.sangwook.shoppingmall.domain.user.dto.UserRegister;
import com.sangwook.shoppingmall.repository.InterestRepository;
import com.sangwook.shoppingmall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final InterestRepository interestRepository;

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
        User user = getUserByEmail(userLogin.getEmail());
        if (!passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) {
            throw new IllegalStateException();
        }
        return user;
    }
    /**
     * 관심사 INTEREST
     */
    public Interest addInterest(Long userId, Category category) {
        User user = getUserById(userId);
        Optional<Interest> getInterest = interestRepository.findByUserIdAndCategory(userId, category);
        if (getInterest.isEmpty()) {
            Interest interest = Interest.interested(user, category);
            return interestRepository.save(interest);
        } else { //이미 해당 카테고리의 관심사가 있는 경우 Preference를 INTERESTED로 변경
            Interest interest = getInterest.get();
            return interest.changeScale(Preference.INTERESTED);
        }
    }

    public void deleteInterest(Long userId, Category category) {
        Interest interest = interestRepository.findByUserIdAndCategory(userId, category).get();
        if (interest.getScale().equals(Preference.INTERESTED)) {
            interestRepository.delete(interest);
        }
    }

    public List<InterestInfo> getInterests(Long userId) {
        List<Interest> interests = interestRepository.findAllById(userId);
        return interests.stream().map(InterestInfo::new).toList();
    }

    /**
     * private method
     */
    private User getUserByEmail(String email) {
        Optional<User> getUser = userRepository.findByEmail(email);
        if (getUser.isEmpty()) {
            throw new IllegalStateException(); //FIXME
        }
        return getUser.get();
    }

    private User getUserById(Long id) {
        Optional<User> getUser = userRepository.findById(id);
        if (getUser.isEmpty()) {
            throw new IllegalStateException(); //FIXME
        }
        return getUser.get();
    }




}
