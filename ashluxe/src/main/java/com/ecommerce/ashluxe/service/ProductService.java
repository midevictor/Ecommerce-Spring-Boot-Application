package com.ecommerce.ashluxe.service;

import com.ecommerce.ashluxe.payload.ProductDTO;
import com.ecommerce.ashluxe.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);
    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);
    ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);
    ProductResponse getProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);
    ProductDTO updateProduct(Long productId, ProductDTO product);
    ProductDTO deleteProduct(Long productId);
    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
