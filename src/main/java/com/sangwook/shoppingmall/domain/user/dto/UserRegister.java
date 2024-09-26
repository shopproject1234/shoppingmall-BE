package com.sangwook.shoppingmall.domain.user.dto;

import com.sangwook.shoppingmall.constant.Gender;
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

    private Integer birth;

    private Gender gender;

    private List<String> category;

    public UserRegister(String email, String nickname, String password, String phoneNumber, Integer birth, Gender gender) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.gender = gender;
    }

    public UserRegister(String email, String nickname, String password, String phoneNumber, Integer birth, Gender gender, List<String> categories) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.gender = gender;
        this.category = categories;
    }
}
