package com.vlad.store.store_management;

import com.vlad.store.store_management.model.Product;
import com.vlad.store.store_management.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        productRepository.deleteAll();

        productRepository.save(new Product(null, "Samsung S22", 1500.0));
        productRepository.save(new Product(null, "iPhone 15", 1700.0));
        productRepository.save(new Product(null, "Laptop Asus", 4200.0));

        System.out.println("▶️ DataInitializer: products inserted");
    }
}
