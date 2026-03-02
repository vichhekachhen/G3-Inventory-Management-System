package com.inventory_pro.system.controller.api;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.inventory_pro.system.entities.StockTransaction;
import com.inventory_pro.system.service.StockService;

@RestController
@RequestMapping("/api/stock")
public class StockApiController {

    @Autowired
    private StockService stockService;

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(
            @RequestParam(value = "draw") int draw,
            @RequestParam(value = "start") int start,
            @RequestParam(value = "length") int length,
            @RequestParam(value = "search[value]", required = false) String searchValue) { // Capture search term

        Pageable pageable = PageRequest.of(start / length, length, Sort.by("transactionDate").descending());

        // Fetch filtered data
        Page<StockTransaction> page = stockService.getTransactionsPage(pageable, searchValue);

        // Map to DTO to ensure username is included as we did before
        List<Map<String, Object>> data = page.getContent().stream().map(tx -> {
            Map<String, Object> map = new HashMap<>();
            map.put("product", Map.of("name", tx.getProduct().getName(), "sku", tx.getProduct().getSku()));
            map.put("type", tx.getType());
            map.put("quantity", tx.getQuantity());
            map.put("transactionDate", tx.getTransactionDate());
            map.put("user", Map.of("username", tx.getUser() != null ? tx.getUser().getUsername() : "System"));
            return map;
        }).collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("draw", draw);
        response.put("recordsTotal", page.getTotalElements());
        response.put("recordsFiltered", page.getTotalElements()); // Important for search count
        response.put("data", data);

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
