package com.vlad.store.store_management.repository;

import com.vlad.store.store_management.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findProductsByCustomCriteria(String namePart, double minPrice) {
        String jpql = "SELECT p FROM Product p WHERE p.name LIKE :name AND p.price > :minPrice";
        TypedQuery<Product> query = entityManager.createQuery(jpql, Product.class);
        query.setParameter("name", "%" + namePart + "%");
        query.setParameter("minPrice", minPrice);
        return query.getResultList();
    }
}
