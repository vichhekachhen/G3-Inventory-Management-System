package com.example.inventory_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.security.Principal;
@Controller
@RequestMapping("/settings")
public class SettingController {

    @GetMapping
    public String showSettingsPage(Model model, Principal principal) {
        // principal.getName() provides the username of the logged-in admin
        model.addAttribute("username", principal.getName());
        model.addAttribute("activePage", "settings");
        return "settings/setting";
    }
}