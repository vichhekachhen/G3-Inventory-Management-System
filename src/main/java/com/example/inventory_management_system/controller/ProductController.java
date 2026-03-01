package com.example.inventory_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.inventory_management_system.entities.Category;
import com.example.inventory_management_system.entities.Product;
import com.example.inventory_management_system.service.CategoryService;
import com.example.inventory_management_system.service.ProductService;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("activePage", "products");
        return "products/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("activePage", "products");
        return "products/form";
    }

    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product, BindingResult result, Model model) {
        if (productService.isSkuTaken(product.getSku(), product.getProductId())) {
            result.rejectValue("sku", "error.product", "SKU is already in use.");
        }

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("activePage", "products");
            return "products/form";
        }

        productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{productId}")
    public String editProduct(@PathVariable("productId") Long productId, Model model) {
        
        Product product = productService.findById(productId);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);


        return "products/edit";
    }

    @PostMapping("/update/{productId}")
    public String updateProduct(@PathVariable Long productId, @ModelAttribute("product") Product product) {
        productService.update(product);
        return "redirect:/products";
    }
}
