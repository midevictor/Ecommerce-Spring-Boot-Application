package com.ecommerce.ashluxe.service;

import com.ecommerce.ashluxe.model.Product;
import com.ecommerce.ashluxe.payload.ProductDTO;
import com.ecommerce.ashluxe.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct( Long categoryId, Product product);
    ProductResponse getAllProducts();
    ProductResponse getProductsByCategory(Long categoryId);
    ProductResponse getProductByKeyword(String keyword);
    ProductDTO updateProduct(Long productId, Product product);
    ProductDTO deleteProduct(Long productId);
}
