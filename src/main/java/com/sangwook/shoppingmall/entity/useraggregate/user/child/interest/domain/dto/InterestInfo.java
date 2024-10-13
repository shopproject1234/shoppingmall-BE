package com.sangwook.shoppingmall.entity.useraggregate.user.child.interest.domain.dto;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.Interest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterestInfo {

    private Category category;

    public InterestInfo(Interest interest) {
        this.category = interest.getCategory();
    }
}
