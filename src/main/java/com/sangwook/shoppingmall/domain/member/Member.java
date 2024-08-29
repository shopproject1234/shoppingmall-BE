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

    public static Member memberRegister(MemberRegister memberRegister) {
        Member member = new Member();
        member.email = memberRegister.getEmail();
        member.name = memberRegister.getName();
        member.password = memberRegister.getPassword();
        member.age = memberRegister.getAge();
        member.gender = memberRegister.getGender();
        if (memberRegister.getFurniture()) {
            member.furniture = Preference.INTERESTED;
        }
        if (memberRegister.getAppliance()) {
            member.appliance = Preference.INTERESTED;
        }
        if (memberRegister.getKitchenware()) {
            member.kitchenware = Preference.INTERESTED;
        }
        if (memberRegister.getDeco()) {
            member.deco = Preference.INTERESTED;
        }
        if (memberRegister.getLighting()) {
            member.lighting = Preference.INTERESTED;
        }
        if (memberRegister.getStorage()) {
            member.storage = Preference.INTERESTED;
        }
        if (memberRegister.getDailyItem()) {
            member.dailyItem = Preference.INTERESTED;
        }
        if (memberRegister.getKids()) {
            member.kids = Preference.INTERESTED;
        }
        if (memberRegister.getCamping()) {
            member.camping = Preference.INTERESTED;
        }
        return member;
    }



    public Member() {

    }
}
