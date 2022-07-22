package com.example.bettominator.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/Logout")
public class LogoutRestController {


    public LogoutRestController() {

    }

    @GetMapping
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/Login";
    }
}
