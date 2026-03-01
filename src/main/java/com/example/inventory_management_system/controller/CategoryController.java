package com.example.inventory_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.inventory_management_system.entities.Category;
import com.example.inventory_management_system.service.CategoryService;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("category", new Category()); // <-- add this
        model.addAttribute("activePage", "categories"); // for sidebar highlighting
        return "categories/category";
    }

    @GetMapping("/add")
    public String showCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("activePage", "categories");
        return "categories/form";
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") Category category) {
        categoryService.saveCategory(category);
        return "redirect:/categories";
    }

    // @GetMapping("/new")
    // public String showCreateForm(Model model) {
    // model.addAttribute("category", new Category());
    // return "category";
    // }

    // @PostMapping
    // public String saveCategory(@ModelAttribute Category category) {
    // categoryService.save(category);
    // return "redirect:/category";
    // }

    // @GetMapping("/edit/{id}")
    // public String showEditForm(@PathVariable Long id, Model model) {
    // model.addAttribute("category", categoryService.findById(id));
    // return "categories/category";
    // }

    // @GetMapping("/delete/{id}")
    // public String deleteCategory(@PathVariable Long id) {
    // categoryService.delete(id);
    // return "redirect:/category";
    // }
}
