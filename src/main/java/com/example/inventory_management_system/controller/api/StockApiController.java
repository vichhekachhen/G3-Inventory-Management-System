package com.example.inventory_management_system.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.inventory_management_system.entities.StockTransaction;
import com.example.inventory_management_system.service.StockService;

@RestController
@RequestMapping("/api/stock")
public class StockApiController {

    @Autowired
    private StockService stockService;

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(
            @RequestParam(value = "draw") int draw,
            @RequestParam(value = "start") int start,
            @RequestParam(value = "length") int length) {
        
        // Use a Pageable object to fetch only the required slice of data
        Pageable pageable = PageRequest.of(start / length, length, Sort.by("transactionDate").descending());
        Page<StockTransaction> page = stockService.getTransactionsPage(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", page.getTotalElements());
        response.put("recordsFiltered", page.getTotalElements());
        response.put("data", page.getContent());

        return ResponseEntity.ok(response);
    }
}
