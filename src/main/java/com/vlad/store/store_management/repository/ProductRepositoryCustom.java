package com.vlad.store.store_management.repository;

import com.vlad.store.store_management.model.Product;
import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> findProductsByCustomCriteria(String namePart, double minPrice);
}
