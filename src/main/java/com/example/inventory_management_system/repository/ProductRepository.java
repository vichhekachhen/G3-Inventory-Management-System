package com.example.inventory_management_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory_management_system.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsBySkuAndProductIdNot(String sku, Long productId);

    boolean existsBySku(String sku);

    List<Product> findByIsDelete(Integer isDelete);
    Optional<Product> findByProductIdAndIsDelete(Long productId, Integer isDelete);

}

