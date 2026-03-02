package com.example.inventory_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.inventory_management_system.service.StockService;


@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired
    private StockService stockService;

    @GetMapping
    public String showReportPage(Model model) {
        // We can pass stock value summaries here in the future
        model.addAttribute("activePage", "reports");
        return "reports/report";
    }
}