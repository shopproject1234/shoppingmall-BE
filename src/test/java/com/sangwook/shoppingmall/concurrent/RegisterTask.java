package com.sangwook.shoppingmall.concurrent;

import com.sangwook.shoppingmall.common.constant.Gender;
import com.sangwook.shoppingmall.application.UserService;
import com.sangwook.shoppingmall.entity.useraggregate.domain.User;
import com.sangwook.shoppingmall.entity.useraggregate.user.dto.UserRegister;

import java.util.List;
import java.util.concurrent.Callable;

public class RegisterTask implements Callable<User> {

    UserService userService;
    public RegisterTask(UserService userService) {
        this.userService = userService;
    }

    UserRegister userRegister = new UserRegister("tkddnr@naver.com",
            "상욱", "123123",
            "01011112222", "2012-11-22",
            Gender.MALE, List.of());

    @Override
    public User call() throws Exception {
        return userService.register(userRegister);
    }
}
