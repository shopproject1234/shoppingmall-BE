package com.sangwook.shoppingmall.domain.member;

import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.constant.Preference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Entity
@Getter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Preference furniture;

    @Enumerated(EnumType.STRING)
    private Preference fabric;

    @Enumerated(EnumType.STRING)
    private Preference appliance;

    @Enumerated(EnumType.STRING)
    private Preference kitchenware;

    @Enumerated(EnumType.STRING)
    private Preference deco;

    @Enumerated(EnumType.STRING)
    private Preference lighting;

    @Enumerated(EnumType.STRING)
    private Preference storage;

    @Enumerated(EnumType.STRING)
    private Preference dailyItem;

    @Enumerated(EnumType.STRING)
    private Preference kids;

    @Enumerated(EnumType.STRING)
    private Preference camping;

}
