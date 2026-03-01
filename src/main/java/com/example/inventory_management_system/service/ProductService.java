package com.example.inventory_management_system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.inventory_management_system.entities.Product;
import com.example.inventory_management_system.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Retrieves all products from MySQL for the main list view.
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Saves a new product or updates an existing one.
     */
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Finds a specific product by its ID for the Edit page.
     */
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * Deletes a product from the database.
     */
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * Optional: Logic to check if a product is low on stock 
     * based on your min_stock_level field.
     */
    public boolean isLowStock(Product product) {
        return product.getCurrentQuantity() <= product.getMinStockLevel();
    }
}