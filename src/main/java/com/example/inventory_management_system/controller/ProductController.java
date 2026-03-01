package com.example.inventory_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.inventory_management_system.entities.Product;
import com.example.inventory_management_system.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listProducts(Model model) {
        // 1. Fetch data from MySQL via the service layer
        List<Product> products = productService.getAllProducts();
        
        // 2. Add the list to the model so Thymeleaf can 'th:each' through it
        model.addAttribute("products", products);
        
        // 3. Set 'activePage' so the sidebar fragment highlights 'Products'
        model.addAttribute("activePage", "products");
        
        // 4. Return the path to your HTML file: src/main/resources/templates/products/list.html
        return "products/list";
    }
}