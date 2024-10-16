package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.entity.itemaggregate.item.domain.Item;
import com.sangwook.shoppingmall.entity.useraggregate.user.domain.dto.EmailCheck;

public interface EmailService {

    void sendVerifyMail(String email);
    void sendItemMail(Item item);
    Boolean verifyCode(EmailCheck check);


}
