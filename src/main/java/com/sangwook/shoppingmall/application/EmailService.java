package com.sangwook.shoppingmall.application;

import com.sangwook.shoppingmall.entity.itemaggregate.domain.Item;
import com.sangwook.shoppingmall.entity.useraggregate.user.dto.EmailCheck;

public interface EmailService {

    void sendVerifyMail(String email);
    void sendItemMail(Item item);
    Boolean verifyCode(EmailCheck check);


}
