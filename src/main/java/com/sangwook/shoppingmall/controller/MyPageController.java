package com.sangwook.shoppingmall.controller;

import com.sangwook.shoppingmall.argumentResolver.Login;
import com.sangwook.shoppingmall.entity.historyaggregate.history.domain.dto.MyPurchase;
import com.sangwook.shoppingmall.entity.itemaggregate.item.child.review.domain.dto.MyReview;
import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.dto.MyItem;
import com.sangwook.shoppingmall.entity.useraggregate.user.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.User;
import com.sangwook.shoppingmall.exception.custom.UserValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.sangwook.shoppingmall.exception.MethodFunction.getMethodName;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user/{user_id}")
public class MyPageController {

    private final UserService userService;

    @GetMapping("/item")
    public ResponseEntity<List<MyItem>> getMyItem(@PathVariable(name = "user_id") Long userId, @Login User user) {
        checkId(userId, user);

        List<MyItem> list = userService.getMyItems(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/purchase")
    public ResponseEntity<List<MyPurchase>> getMyPurchase(@PathVariable(name = "user_id") Long userId, @Login User user) {
        checkId(userId, user);

        List<MyPurchase> list = userService.getMyPurchases(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/review")
    public ResponseEntity<List<MyReview>> getMyReview(@PathVariable(name = "user_id") Long userId, @Login User user) {
        checkId(userId, user);

        List<MyReview> list = userService.getMyReviews(userId);
        return ResponseEntity.ok(list);
    }

    private void checkId(Long userId, User user) {
        if (!user.getId().equals(userId)) {
            throw new UserValidationException("유저 검증에 실패하였습니다.", getMethodName());
        }
    }
}
