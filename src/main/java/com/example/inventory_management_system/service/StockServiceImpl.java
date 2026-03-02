package com.example.inventory_management_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory_management_system.entities.Product;
import com.example.inventory_management_system.entities.StockTransaction;
import com.example.inventory_management_system.model.TransactionType;
import com.example.inventory_management_system.repository.ProductRepository;
import com.example.inventory_management_system.repository.StockTransactionRepository;
import com.example.inventory_management_system.repository.UserRepository;

@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StockTransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;

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

    @Override
    public Page<StockTransaction> getTransactionsPage(Pageable pageable, String search) {
        if (search != null && !search.isEmpty()) {
            return transactionRepository.searchTransactions(search, pageable);
        }
        return transactionRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void processStockIn(StockTransaction transaction, String username) {
        // 1. Fetch the managed product entity
        Product product = productRepository.findById(transaction.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2. Assign the user and transaction type
        transaction.setUser(userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found")));
        transaction.setType(TransactionType.STOCK_IN);

        // 3. Update the Product Quantity
        product.setCurrentQuantity(product.getCurrentQuantity() + transaction.getQuantity());

        // 4. Save both entities
        productRepository.save(product);
        transactionRepository.save(transaction); // Return the saved object to match interface
        // return 
    }

    @Override
    @Transactional
    public StockTransaction processStockOut(StockTransaction transaction, String username) {
        // 1. Fetch the product
        Product product = productRepository.findById(transaction.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2. Validation: Prevent negative stock
        if (product.getCurrentQuantity() < transaction.getQuantity()) {
            throw new RuntimeException("Insufficient stock! Available: " + product.getCurrentQuantity());
        }

        // 3. Assign User and Type
        transaction.setUser(userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found")));
        transaction.setType(TransactionType.STOCK_OUT);

        // 4. Decrease Quantity
        product.setCurrentQuantity(product.getCurrentQuantity() - transaction.getQuantity());

        // 5. Save changes
        productRepository.save(product);
        return transactionRepository.save(transaction);
    }
}
