package com.inventory_pro.system.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.inventory_pro.system.entities.Product;
import com.inventory_pro.system.repository.ProductRepository;

@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String showReportPage(Model model) {
        List<Product> products = productRepository.findAll();

        // Total products
        long totalProducts = products.size();

        // Stock value = sum(price * currentQuantity)
        BigDecimal stockValue = products.stream()
                .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getCurrentQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Low stock = count where currentQuantity < minStockLevel
        long lowStockCount = products.stream()
                .filter(p -> p.getCurrentQuantity() < p.getMinStockLevel())
                .count();

        // Expiring soon = count where expiryDate is within 2 months
        LocalDate now = LocalDate.now();
        long expiringSoonCount = products.stream()
                .filter(p -> p.getExpiryDate() != null &&
                        !p.getExpiryDate().isBefore(now) &&
                        p.getExpiryDate().isBefore(now.plusMonths(2)))
                .count();

        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("stockValue", stockValue);
        model.addAttribute("lowStockCount", lowStockCount);
        model.addAttribute("expiringSoonCount", expiringSoonCount);

        // Find low stock products
        List<Product> lowStockProducts = products.stream()
                .filter(p -> p.getCurrentQuantity() < p.getMinStockLevel())
                .toList();

        // Only pass the count, not the list
        model.addAttribute("lowStockCount", lowStockProducts.size());
        model.addAttribute("activePage", "reports");

        return "reports/report";


        

    }

}