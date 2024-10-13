package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.entity.useraggregate.user.domain.dto.EmailCheck;

public interface EmailService {

    void sendMail(String email);
    Boolean verifyCode(EmailCheck check);

}
