package com.example.inventory_management_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory_management_system.entities.Product;
import com.example.inventory_management_system.entities.StockTransaction;
import com.example.inventory_management_system.repository.ProductRepository;
import com.example.inventory_management_system.repository.StockTransactionRepository;

@Service
public class StockServiceImpl implements StockService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockTransactionRepository transactionRepository;

    @Override
    @Transactional
    public void processTransaction(StockTransaction tx) {
        Product product = tx.getProduct();
        
        // Logic handles the 4 specific types from your MySQL schema
        switch (tx.getType()) {
            case STOCK_IN:
            case ADJUSTMENT: // Assuming adjustment here is positive; you can add logic for negative
                product.setCurrentQuantity(product.getCurrentQuantity() + tx.getQuantity());
                break;
            case STOCK_OUT:
            case SALE:
                if (product.getCurrentQuantity() < tx.getQuantity()) {
                    throw new RuntimeException("Insufficient stock for " + product.getName());
                }
                product.setCurrentQuantity(product.getCurrentQuantity() - tx.getQuantity());
                break;
        }
        
        transactionRepository.save(tx);
        productRepository.save(product);
    }

    @Override
    public List<StockTransaction> getRecentTransactions() {
        return transactionRepository.findAllByOrderByTransactionDateDesc();
    }

    @Override
    public Page<StockTransaction> getTransactionsPage(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }
}
