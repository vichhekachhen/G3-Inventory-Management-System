package com.inventory_pro.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inventory_pro.system.service.ProductService;

@Controller
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private ProductService productService; // Assuming you have this to fetch product list

    @GetMapping
    public String showSalePage(Model model) {
        // Fetch products so the dropdown in sale.html has data
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("activePage", "sales");
        return "sales/sale"; 
    }
}
