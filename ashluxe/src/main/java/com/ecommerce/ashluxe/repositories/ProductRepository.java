package com.ecommerce.ashluxe.repositories;

import com.ecommerce.ashluxe.model.Category;
import com.ecommerce.ashluxe.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryOrderByPriceAsc(Category category);
    List<Product> findByProductNameContainingIgnoreCase(String keyword);
}
