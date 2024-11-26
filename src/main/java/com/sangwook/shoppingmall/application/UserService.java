package com.sangwook.shoppingmall.application;

import com.sangwook.shoppingmall.common.constant.Category;
import com.sangwook.shoppingmall.common.constant.Preference;
import com.sangwook.shoppingmall.entity.historyaggregate.history.dto.MyPurchase;
import com.sangwook.shoppingmall.entity.historyaggregate.history.infra.HistoryRepository;
import com.sangwook.shoppingmall.entity.itemaggregate.review.dto.MyReview;
import com.sangwook.shoppingmall.entity.itemaggregate.review.infra.ReviewRepository;
import com.sangwook.shoppingmall.entity.itemaggregate.item.dto.MyItem;
import com.sangwook.shoppingmall.entity.itemaggregate.item.infra.ItemRepository;
import com.sangwook.shoppingmall.entity.useraggregate.domain.Interest;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.dto.*;
import com.sangwook.shoppingmall.exception.custom.ObjectNotFoundException;
import com.sangwook.shoppingmall.exception.custom.UserValidationException;
import com.sangwook.shoppingmall.entity.useraggregate.user.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ItemRepository itemRepository;
    private final HistoryRepository historyRepository;
    private final ReviewRepository reviewRepository;

    public User register(UserRegister userRegister) {
        emailCheck(userRegister.getEmail());
        String encoded = passwordEncoder.encode(userRegister.getPassword());
        User user = User.register(userRegister, encoded); // 비밀번호 encoding하여 저장
        user.addInterest(userRegister.getCategory());
        userRepository.save(user);
        return user;
    }

    public User login(UserLogin userLogin) {
        User user = getUserByEmail(userLogin.getEmail());
        if (!passwordEncoder.matches(userLogin.getPassword(), user.getPassword())) { // 요청온 비밀번호와 저장된 비밀번호가 다른경우
            throw new UserValidationException("비밀번호가 잘못되었습니다", getMethodName());
        }
        return user;
    }

    /**
     * 사용자 정보
     */
    public UserInfo getUserInfo(Long userId) {
        User user = getUserById(userId);
        List<Interest> interests = user.getInterests();
        List<Category> interested = new ArrayList<>(); //category를 담을 List
        UserInfo info = new UserInfo();
        info.setNickname(user.getName());

        for (Interest interest : interests) {
            if (interest.getScale().equals(Preference.INTERESTED)) {
                interested.add(interest.getCategory());
            }
        }
        info.setCategory(interested);
        return info;
    }

    public User changeUserInfo(Long userId, UserInfo userInfo) {
        User user = getUserById(userId);
        user = user.changeUserInfo(userInfo);
        return userRepository.save(user);
    }

    public Boolean checkPass(PassCheck passCheck, User getUser) {
        User user = getUserByEmail(getUser.getEmail());
        return passwordEncoder.matches(passCheck.getPassword(), user.getPassword());
    }

    public Boolean checkUserExist(String email) {
        Optional<User> getUser = userRepository.findByEmail(email);
        return getUser.isPresent();
    }

    public Boolean validEmail(EmailValid emailValid) {
        Optional<User> getUser = userRepository.findByEmail(emailValid.getEmail());
        return getUser.isEmpty();
    }

    /**
     * MyPage
     */
    public List<MyItem> getMyItems(Long userId) {
        return itemRepository.findMyItems(userId);
    }

    public List<MyPurchase> getMyPurchases(Long userId) {
        return historyRepository.findMyPurchases(userId);
    }

    public List<MyReview> getMyReviews(Long userId) {
        return reviewRepository.findMyReviews(userId);
    }

    /**
     * private method
     */
    private User getUserByEmail(String email) {
        Optional<User> getUser = userRepository.findByEmail(email);
        if (getUser.isEmpty()) {
            throw new ObjectNotFoundException(getMethodName());
        }
        return getUser.get();
    }

    private User getUserById(Long id) {
        Optional<User> getUser = userRepository.findById(id);
        if (getUser.isEmpty()) {
            throw new ObjectNotFoundException(getMethodName());
        }
        return getUser.get();
    }

    private void emailCheck(String email) {
        Optional<User> getUser = userRepository.findByEmail(email);
        if (getUser.isPresent()) { // 이미 해당 이메일을 가진 유저가 존재하는 경우
            throw new UserValidationException("해당 유저는 이미 존재합니다", getMethodName());
        }
    }
}
