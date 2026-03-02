package com.inventory_pro.system.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory_pro.system.entities.Product;
import com.inventory_pro.system.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findByIsDelete(0);
    }

    public Product findById(Long id) {
        return productRepository.findByProductIdAndIsDelete(id, 0)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void update(Product product) {
        productRepository.save(product);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public boolean isLowStock(Product product) {
        return product.getCurrentQuantity() <= product.getMinStockLevel();
    }

    public boolean isSkuTaken(String sku, Long id) {
        if (id == null) {
            return productRepository.existsBySku(sku);
        }
        return productRepository.existsBySkuAndProductIdNot(sku, id);
    }

    
    // Soft delete product (set isDelete = true)
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setIsDelete(1); // mark as deleted
        productRepository.save(product);
    }
}
