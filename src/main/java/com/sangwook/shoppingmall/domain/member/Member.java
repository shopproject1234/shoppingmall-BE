package com.sangwook.shoppingmall.domain.member;

import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.constant.Preference;
import com.sangwook.shoppingmall.domain.member.dto.MemberRegister;
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

    public static Member memberRegister(MemberRegister memberRegister, String password) {
        Member member = new Member();
        member.email = memberRegister.getEmail();
        member.name = memberRegister.getName();
        member.password = password;
        member.age = memberRegister.getAge();
        member.gender = memberRegister.getGender();
        if (memberRegister.isFurniture()) {
            member.furniture = Preference.INTERESTED;
        }
        if (memberRegister.isAppliance()) {
            member.appliance = Preference.INTERESTED;
        }
        if (memberRegister.isKitchenware()) {
            member.kitchenware = Preference.INTERESTED;
        }
        if (memberRegister.isDeco()) {
            member.deco = Preference.INTERESTED;
        }
        if (memberRegister.isLighting()) {
            member.lighting = Preference.INTERESTED;
        }
        if (memberRegister.isStorage()) {
            member.storage = Preference.INTERESTED;
        }
        if (memberRegister.isDailyItem()) {
            member.dailyItem = Preference.INTERESTED;
        }
        if (memberRegister.isKids()) {
            member.kids = Preference.INTERESTED;
        }
        if (memberRegister.isCamping()) {
            member.camping = Preference.INTERESTED;
        }
        return member;
    }



    public Member() {

    }
}
