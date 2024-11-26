package com.sangwook.shoppingmall.common.eventListener;

import com.sangwook.shoppingmall.application.EmailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Getter
public class EmailEventListener {

    private final EmailService emailService;

    @EventListener
    public void sendItemQuantityMail(ItemEvent event) {
        emailService.sendItemMail(event.getItem());
    }
}
