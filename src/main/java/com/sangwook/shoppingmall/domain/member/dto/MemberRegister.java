package com.sangwook.shoppingmall.domain.member.dto;

import com.sangwook.shoppingmall.constant.Gender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class MemberRegister {
    
    @NotEmpty(message = "이메일을 적어주세요")
    private String email;

    @NotEmpty(message = "이름을 적어주세요")
    private String name;

    @NotEmpty(message = "비밀번호를 입력해주세요, 5~15")
    @Size(min = 5, max = 15, message = "5~15자리로 작성해야합니다")
    private String password;

    @NotNull(message = "나이는 필수항목입니다")
    private Integer age;

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
