package com.example.inventory_management_system.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.inventory_management_system.entities.StockTransaction;

public interface StockService {
    void processTransaction(StockTransaction tx);
    List<StockTransaction> getRecentTransactions();
    Page<StockTransaction> getTransactionsPage(Pageable pageable);

}
