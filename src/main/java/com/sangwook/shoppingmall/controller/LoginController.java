package com.sangwook.shoppingmall.controller;

import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.domain.member.dto.MemberLogin;
import com.sangwook.shoppingmall.domain.member.dto.MemberRegister;
import com.sangwook.shoppingmall.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/")
    public String getLogin(Model model) {
        model.addAttribute("memberLogin", new MemberLogin());
        return "member/login";
    }

    @PostMapping("/member/getLogin")
    public void login(@ModelAttribute("memberLogin") MemberLogin memberLogin) {
        memberService.login(memberLogin);
    }

    @GetMapping("/member/getRegister")
    public String getRegister(Model model) {
        model.addAttribute("memberRegister", new MemberRegister());
        model.addAttribute("genders", Gender.values());
        return "member/register";
    }

    @PostMapping("/member/register")
    public String register(@Valid @ModelAttribute("memberRegister") MemberRegister memberRegister, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("genders", Gender.values());
            return "member/register";
        }

        return "a";
    }
}
