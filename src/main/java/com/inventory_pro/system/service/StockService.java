package com.inventory_pro.system.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inventory_pro.system.entities.StockTransaction;

public interface StockService {
    void processTransaction(StockTransaction tx);
    List<StockTransaction> getRecentTransactions();
    Page<StockTransaction> getTransactionsPage(Pageable pageable);
    Page<StockTransaction> getTransactionsPage(Pageable pageable, String search);
    void processStockIn(StockTransaction transaction, String username);
    StockTransaction processStockOut(StockTransaction transaction, String username);

}
