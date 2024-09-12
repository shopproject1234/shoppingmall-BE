package com.sangwook.shoppingmall.domain.interest.dto;

import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.domain.interest.Interest;
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
