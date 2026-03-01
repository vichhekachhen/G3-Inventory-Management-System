package com.example.inventory_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory_management_system.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}