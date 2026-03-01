package com.example.inventory_management_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory_management_system.entities.StockTransaction;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    // Standard JpaRepository includes findAll(Pageable pageable) automatically

    List<StockTransaction> findAllByOrderByTransactionDateDesc();
}
