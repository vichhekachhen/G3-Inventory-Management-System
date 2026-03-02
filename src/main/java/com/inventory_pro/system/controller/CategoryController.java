package com.inventory_pro.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.inventory_pro.system.entities.Category;
import com.inventory_pro.system.service.CategoryService;

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
        return "categories/list";
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

    @GetMapping("/edit/{categoryId}")
    public String showEditForm(@PathVariable("categoryId") Long categoryId, Model model) {
        Category category = categoryService.findById(categoryId);
        model.addAttribute("category", category);
        return "categories/edit"; // must match your template filename
    }

    @PostMapping("/update/{categoryId}")
    public String updateCategory(@PathVariable Long categoryId, @ModelAttribute("category") Category category) {
        categoryService.update(categoryId, category);
        return "redirect:/categories";
    }

    @PostMapping("/delete/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteById(categoryId);
        return "redirect:/categories";
    }

}
