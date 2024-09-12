package com.sangwook.shoppingmall.domain.user.dto;

import com.sangwook.shoppingmall.constant.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRegister {

    private String email;

    private String nickname;

    private String password;

    private String phoneNumber;

    private LocalDate birth;

    private Integer age;

    private Gender gender;

    public UserRegister(String email, String nickname, String password, String phoneNumber, LocalDate birth, Integer age, Gender gender) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birth = birth;
        this.age = age;
        this.gender = gender;
    }
}
