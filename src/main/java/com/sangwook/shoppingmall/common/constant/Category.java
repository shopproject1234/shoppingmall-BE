package com.sangwook.shoppingmall.common.constant;

public enum Category {
    FURNITURE("가구"),
    FABRIC("직물"),
    APPLIANCE("가전"),
    KITCHENWARE("주방용품"),
    DECO("데코"),
    LIGHTING("조명"),
    STORAGE("수납"),
    DAILYITEM("일상용품"),
    KIDS("키즈"),
    CAMPING("캠핑");

    public final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }
}
