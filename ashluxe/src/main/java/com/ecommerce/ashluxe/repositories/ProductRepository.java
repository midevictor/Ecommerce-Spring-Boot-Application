package com.ecommerce.ashluxe.repositories;

import com.ecommerce.ashluxe.model.Category;
import com.ecommerce.ashluxe.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);
    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageDetails);
}
