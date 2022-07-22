package com.example.bettominator.web;

import com.example.bettominator.model.User;
import com.example.bettominator.model.exceptions.LoginCredentialsException;
import com.example.bettominator.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/Login")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }


    @GetMapping
    public String getLoginPage(@RequestParam(required = false) String info,
                               Model model) {
        model.addAttribute("info", info);
        return "Login";
    }


    public String login(HttpServletRequest request, Model model) {
        User user = null;
        try {
            user = this.authService.login(request.getParameter("username"),
                    request.getParameter("password"));
            request.getSession().setAttribute("user", user);
            return "redirect:/Home";
        } catch (LoginCredentialsException exception) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", exception.getMessage());
            return "Login";

        }
    }


}