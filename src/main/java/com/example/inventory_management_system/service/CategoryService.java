package com.example.inventory_management_system.service;

import org.springframework.stereotype.Service;

import com.example.inventory_management_system.entities.Category;
import com.example.inventory_management_system.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findByStatus(1);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Category findById(Long id) {
        return categoryRepository
                .findByCategoryIdAndStatus(id, 1)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public void update(Long id, Category updatedCategory) {
        Category existing = findById(id);
        existing.setName(updatedCategory.getName());
        existing.setDescription(updatedCategory.getDescription());
        categoryRepository.save(existing);
    }

    public void deleteById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setStatus(0);
        categoryRepository.save(category);
    }

}
