package com.sangwook.shoppingmall.controller;

import com.sangwook.shoppingmall.constant.Gender;
import com.sangwook.shoppingmall.constant.SessionConst;
import com.sangwook.shoppingmall.domain.member.Member;
import com.sangwook.shoppingmall.domain.member.dto.MemberLogin;
import com.sangwook.shoppingmall.domain.member.dto.MemberRegister;
import com.sangwook.shoppingmall.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

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

    @PostMapping("/member/login")
    public String login(@ModelAttribute("memberLogin") MemberLogin memberLogin, HttpServletRequest request) {
        Optional<Member> member = memberService.login(memberLogin.getEmail());
        if (member.isEmpty()) {
            throw new IllegalStateException();
        }
        if (!member.get().getPassword().equals(memberLogin.getPassword())) {
            throw new IllegalStateException();
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);

        return "redirect:/shop/main";
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
        memberService.register(memberRegister);

        //TODO 회원가입 후 alert창 하나 띄우면 좋을듯?
        return "redirect:/";
    }
}
