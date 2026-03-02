package com.inventory_pro.system.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inventory_pro.system.entities.StockTransaction;
import com.inventory_pro.system.service.ProductService;


@Controller
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String stockManagement(Model model) {
        model.addAttribute("activePage", "stocks");
        model.addAttribute("transaction", new StockTransaction());
        model.addAttribute("products", productService.getAllProducts());
        // The history table will be loaded via AJAX from your RestController
        return "stock/management";
    }

    @GetMapping("/list")
    public String listStock(Model model) {
        model.addAttribute("activePage", "stocks");
        model.addAttribute("transactions", new ArrayList<StockTransaction>());
        return "stock/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        return "stock/form";
    }

}
