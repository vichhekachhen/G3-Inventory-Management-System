package com.inventory_pro.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String login() {
        return "login"; // matches login.html
    }

    @GetMapping("/home")
    public String dashboard() {
        return "dashboard"; // create a simple dashboard.html next
    }
}