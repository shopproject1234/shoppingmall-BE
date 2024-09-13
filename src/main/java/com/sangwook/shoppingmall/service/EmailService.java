package com.sangwook.shoppingmall.service;

import com.sangwook.shoppingmall.domain.user.dto.EmailCheck;

public interface EmailService {

    void sendMail(String email);
    Boolean verifyCode(EmailCheck check);

}
