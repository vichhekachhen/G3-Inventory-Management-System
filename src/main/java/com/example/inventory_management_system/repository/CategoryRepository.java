package com.example.inventory_management_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.inventory_management_system.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByStatus(Integer status);
    Optional<Category> findByCategoryIdAndStatus(Long categoryId, Integer status);
}