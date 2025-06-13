package com.vlad.store.store_management.controller;

import com.vlad.store.store_management.model.Product;
import com.vlad.store.store_management.service.ProductService;
import com.vlad.store.store_management.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Get product by id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new ProductNotFoundException("Product with id " + id + " not found.");
        }
        return ResponseEntity.ok(product);
    }

    // Create new product
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product created = productService.addProduct(product);
        return ResponseEntity.ok(created);
    }

    // Update existing product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        if (updated == null) {
            throw new ProductNotFoundException("Product with id " + id + " not found.");
        }
        return ResponseEntity.ok(updated);
    }

    // Delete product by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException ex) {
            throw new ProductNotFoundException("Product with id " + id + " not found.");
        }
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<Product> updatePrice(@PathVariable Long id, @RequestParam Double price) {
        try {
            Product updatedProduct = productService.updateProductPrice(id, price);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            throw new ProductNotFoundException("Product with id " + id + " not found.");
        }
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<Product> getProductByName(@PathVariable String name) {
        return productService.getProductByName(name)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ProductNotFoundException("Product with name " + name + " not found."));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(
            @RequestParam String namePart,
            @RequestParam double minPrice) {

        List<Product> products = productService.getProductsByCustomCriteria(namePart, minPrice);
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(products);
    }

    // Handle ProductNotFoundException locally
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Handle any other exceptions locally
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
