//package com.example.bettominator.web.rest;
//
//
//import com.example.bettominator.model.User;
//import com.example.bettominator.service.UserService;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/Users")
//public class UsersRestController {
//
//    private final UserService userService;
//
//    public UsersRestController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping("/{id}")
//    public User findById(@PathVariable String id) {
//        return this.userService.findById(id);
//    }
//
//
//}