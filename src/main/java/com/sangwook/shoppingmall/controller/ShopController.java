package com.sangwook.shoppingmall.controller;

import com.sangwook.shoppingmall.argumentResolver.Login;
import com.sangwook.shoppingmall.constant.Category;
import com.sangwook.shoppingmall.domain.item.Item;
import com.sangwook.shoppingmall.domain.item.dto.AddItem;
import com.sangwook.shoppingmall.domain.item.dto.ItemInfo;
import com.sangwook.shoppingmall.domain.member.Member;
import com.sangwook.shoppingmall.domain.purchase.dto.PurchaseInfo;
import com.sangwook.shoppingmall.domain.purchase.dto.PurchaseSubmit;
import com.sangwook.shoppingmall.service.ShopService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/shop/info/{itemId}")
    public String itemInfo(@PathVariable Long itemId, @Login Member member, Model model) {
        Item item = shopService.findItemById(itemId);
        if (item.getMember().getId().equals(member.getId())) {
            model.addAttribute("mine", true); //본인의 상품인지 확인 (수정, 삭제에 사용)
        } else {
            model.addAttribute("mine", false);
        }
        ItemInfo itemInfo = new ItemInfo(item, member);
        model.addAttribute("itemInfo", itemInfo);
        return "shop/itemInfo";
    }

    @GetMapping("/shop/purchase/{itemId}")
    public String purchaseForm(@PathVariable Long itemId, @Login Member member, Model model) {
        Item item = shopService.findItemById(itemId);
        PurchaseInfo info = new PurchaseInfo(itemId, member.getName(), item.getItemCount(), item.getCategory(), item.getPrice(), item.getName());
        model.addAttribute("purchaseInfo", info);
        model.addAttribute("purchaseSubmit", new PurchaseSubmit());
        return "shop/purchaseInfo";
    }

    @PostMapping("/shop/purchase/{itemId}")
    public String purchaseItem(@PathVariable Long itemId, @Login Member member, @ModelAttribute(name = "purchaseSubmit") PurchaseSubmit purchaseSubmit) {
        shopService.purchase(itemId, member, purchaseSubmit.getCount());
        return "redirect:/shop/main";
    }
}
