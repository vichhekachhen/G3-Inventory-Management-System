package com.example.inventory_management_system.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.inventory_management_system.entities.StockTransaction;

public interface StockService {
    void processTransaction(StockTransaction tx);
    List<StockTransaction> getRecentTransactions();
    Page<StockTransaction> getTransactionsPage(Pageable pageable);
    void processStockIn(StockTransaction transaction, String username);
    StockTransaction processStockOut(StockTransaction transaction, String username);

}
