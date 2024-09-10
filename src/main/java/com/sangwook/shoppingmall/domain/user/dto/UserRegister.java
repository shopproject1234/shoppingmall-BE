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

}
