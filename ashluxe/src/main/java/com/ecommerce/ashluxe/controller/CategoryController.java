package com.ecommerce.ashluxe.controller;

import com.ecommerce.ashluxe.config.AppConstants;
import com.ecommerce.ashluxe.model.Category;
import com.ecommerce.ashluxe.payload.CategoryDTO;
import com.ecommerce.ashluxe.payload.CategoryResponse;
import com.ecommerce.ashluxe.service.CategoryService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CategoryController {
//    private List<Category> categories = new ArrayList<>();
    @Autowired
    private CategoryService categoryService;

    @GetMapping("api/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_CATEGORIES_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.DEFAULT_SORT_DIR, required = false) String sortOrder

    ){
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber, pageSize, sortOrder, sortBy);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK) ;
    }


    @PostMapping("api/admin/categories")
    public ResponseEntity<CategoryDTO> addCategory( @Valid @RequestBody CategoryDTO categoryDTO) {
       CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED) ;
    }

    @DeleteMapping("api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId){

            CategoryDTO deletedCategory = categoryService.deleteCategory(categoryId);
//            return  ResponseEntity.status(HttpStatus.OK).body(status);
        return new ResponseEntity<>(deletedCategory, HttpStatus.OK);

    }

    @PutMapping("api/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateMapping( @Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId){

            CategoryDTO updatedCategoryDTO = categoryService.updateCategory(categoryDTO, categoryId);
            return new ResponseEntity<>(updatedCategoryDTO, HttpStatus.OK);

    }


}
