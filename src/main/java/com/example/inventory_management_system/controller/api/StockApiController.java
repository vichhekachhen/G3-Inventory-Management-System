package com.example.inventory_management_system.controller.api;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/save")
    public ResponseEntity<?> saveTransaction(@ModelAttribute StockTransaction transaction, Principal principal) {
        try {
            // Ensure the service method handles the stock logic and returns the saved object
            stockService.processStockIn(transaction, principal.getName());
            return ResponseEntity.ok("Success");
        } catch (RuntimeException e) {
            // Return 400 Bad Request so SweetAlert shows the error icon
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Return 500 for system failures
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @PostMapping("/out")
    public ResponseEntity<?> stockOut(@ModelAttribute StockTransaction transaction, Principal principal) {
        try {
            stockService.processStockOut(transaction, principal.getName());
            return ResponseEntity.ok("Stock reduced successfully");
        } catch (RuntimeException e) {
            // This sends the "Insufficient stock" message back to the SweetAlert
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
