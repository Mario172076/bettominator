package com.example.bettominator.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/Logout")
public class LogoutController {
    @GetMapping
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/Login";
    }
}
