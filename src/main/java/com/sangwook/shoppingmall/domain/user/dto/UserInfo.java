package com.sangwook.shoppingmall.domain.user.dto;

import com.sangwook.shoppingmall.constant.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserInfo {

    private List<Category> category;

    private String nickname;
}
