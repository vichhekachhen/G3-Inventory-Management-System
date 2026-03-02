package com.inventory_pro.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inventory_pro.system.entities.User;
import com.inventory_pro.system.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<User> userPage = userService.getAllUsers(PageRequest.of(page, size));
         model.addAttribute("activePage", "categories"); // for sidebar highlighting
        model.addAttribute("userPage", userPage);
        return "users/list"; // Thymeleaf template
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "users/create"; // Thymeleaf template name
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/users"; // Thymeleaf template name
    }

    // Show edit form
    @GetMapping("/edit/{userId}")
    public String showEditForm(@PathVariable("userId") Long userId, Model model) {
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        return "users/edit"; // must match your Thymeleaf template filename
    }

    // Handle update
    @PostMapping("/update/{userId}")
    public String updateUser(@PathVariable Long userId, @ModelAttribute("user") User user) {
        userService.update(userId, user);
        return "redirect:/users";
    }

  // Delete (disable) user
    @PostMapping("/delete/{userId}")
    public String deleteUser(@PathVariable("userId") Long userId) {
        userService.disableUser(userId);
        return "redirect:/users";
    }
}
