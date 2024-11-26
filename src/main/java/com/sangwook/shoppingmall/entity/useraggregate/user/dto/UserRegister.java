package com.sangwook.shoppingmall.entity.useraggregate.user.dto;

import com.sangwook.shoppingmall.common.constant.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRegister {

    private String email;

    private String nickname;

    private String password;

    private String phoneNumber;

    private String birth;

    private Gender gender;

    private List<String> category;

    public UserRegister(String email, String nickname, String password, String phoneNumber, String birth, Gender gender, List<String> categories) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.gender = gender;
        this.category = categories;
    }

    public UserRegister() {
    }
}
