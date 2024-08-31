package com.sangwook.shoppingmall.controller;

import com.sangwook.shoppingmall.argumentResolver.Login;
import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.member.Member;
import com.sangwook.shoppingmall.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/shop/add") // 상품 추가
    public String addItemForm(Model model) {
        model.addAttribute("categories", Category.values());
        model.addAttribute("addItem", new AddItem());
        return "shop/addItem";
    }

    @PostMapping("/shop/add")
    public String addItem(@Login Member member, @ModelAttribute("addItem") AddItem addItem) {
        shopService.add(addItem, member);
        return "redirect:/shop/main";
    }

    @GetMapping("/shop/main") // shop 메인화면
    public String shopMain(Model model, Pageable pageable) {
        Page<Item> randomItem = shopService.getRandomItem(pageable);
        model.addAttribute("randomItem", randomItem);
        model.addAttribute("점수 순대로 정렬된 목록"); //TODO Batch로 정렬된 목록 추가
        return "shop/main";
    }
}
