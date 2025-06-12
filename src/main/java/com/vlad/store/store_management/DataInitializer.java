package com.vlad.store.store_management;

import com.vlad.store.store_management.model.Product;
import com.vlad.store.store_management.model.User;
import com.vlad.store.store_management.repository.ProductRepository;
import com.vlad.store.store_management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    public DataInitializer(ProductRepository productRepository,
                           UserRepository userRepository
                           /*PasswordEncoder passwordEncoder*/) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Clear DB
        productRepository.deleteAll();
        userRepository.deleteAll();

        // Insert products
        productRepository.save(new Product(null, "Samsung S22", 1500.0));
        productRepository.save(new Product(null, "iPhone 15", 1700.0));
        productRepository.save(new Product(null, "Laptop Asus", 4200.0));

        // Insert users with encoded passwords
        userRepository.save(new User(null, "user1","userpass", "ROLE_USER"));
        userRepository.save(new User(null, "admin1", "adminpass", "ROLE_ADMIN"));

        System.out.println("▶️ DataInitializer: products and users inserted");
    }
}
