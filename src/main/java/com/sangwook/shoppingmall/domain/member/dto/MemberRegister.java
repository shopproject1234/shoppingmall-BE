package com.sangwook.shoppingmall.domain.member.dto;

import com.sangwook.shoppingmall.constant.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MemberRegister {
    
    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    private Integer age;

    @NotNull
    private Gender gender;

    private Boolean furniture;
    private Boolean fabric;
    private Boolean appliance;
    private Boolean kitchenware;
    private Boolean deco;
    private Boolean lighting;
    private Boolean storage;
    private Boolean dailyItem;
    private Boolean kids;
    private Boolean camping;

    //어떻게하면 선호제품을 효과적으로 받을 수 있을까
}
