package com.sangwook.shoppingmall.entity.useraggregate.interest.dto;

import com.sangwook.shoppingmall.common.constant.Category;
import com.sangwook.shoppingmall.entity.useraggregate.domain.Interest;
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
