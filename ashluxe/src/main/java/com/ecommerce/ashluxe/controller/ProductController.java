package com.ecommerce.ashluxe.controller;


import com.ecommerce.ashluxe.config.AppConstants;
import com.ecommerce.ashluxe.model.Product;
import com.ecommerce.ashluxe.payload.ProductDTO;
import com.ecommerce.ashluxe.payload.ProductResponse;
import com.ecommerce.ashluxe.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
    // constructor injection

    private final ProductService productService;
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> addProduct(@Valid  @RequestBody ProductDTO productDTO, @PathVariable Long categoryId){
       ProductDTO savedProductDTO = productService.addProduct(categoryId, productDTO);
       return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public  ResponseEntity<ProductResponse> getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortOrder
    ){
        ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortOrder, sortBy);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    @GetMapping("/public/categories/{categoryId}/products")
    public  ResponseEntity<ProductResponse> getProductsByCategory( @PathVariable Long categoryId,  @RequestParam(name = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                                   @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                                   @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                   @RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortOrder
    ){
        ProductResponse productResponse = productService.getProductsByCategory(categoryId, pageNumber, pageSize, sortOrder, sortBy);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductByKeyword(@PathVariable String keyword,  @RequestParam(name = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
                                                               @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
                                                               @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_PRODUCTS_BY, required = false) String sortBy,
                                                               @RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortOrder){
        ProductResponse productResponse = productService.getProductByKeyword(keyword, pageNumber, pageSize, sortOrder, sortBy);
        return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @PathVariable Long productId, @RequestBody ProductDTO productDTO){
        ProductDTO updatedProduct = productService.updateProduct(productId, productDTO);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
       ProductDTO deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);
    }

    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException {
        ProductDTO updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
