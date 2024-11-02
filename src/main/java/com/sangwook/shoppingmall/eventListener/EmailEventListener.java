package com.sangwook.shoppingmall.eventListener;

import com.sangwook.shoppingmall.service.EmailService;
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
