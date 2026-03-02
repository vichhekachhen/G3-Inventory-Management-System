package com.example.inventory_management_system.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.inventory_management_system.entities.StockTransaction;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    // Standard JpaRepository includes findAll(Pageable pageable) automatically
    String SEARCH_QUERY = "SELECT st FROM StockTransaction st WHERE "
            + "LOWER(st.product.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(st.product.sku) LIKE LOWER(CONCAT('%', :searchTerm, '%'))";


    List<StockTransaction> findAllByOrderByTransactionDateDesc();

    @Query(SEARCH_QUERY) // This works because SEARCH_QUERY is a constant
    Page<StockTransaction> searchTransactions(@Param("searchTerm") String searchTerm, Pageable pageable);
}
