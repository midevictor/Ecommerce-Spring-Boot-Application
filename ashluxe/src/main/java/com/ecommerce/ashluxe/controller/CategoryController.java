package com.ecommerce.ashluxe.controller;

import com.ecommerce.ashluxe.model.Category;
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
    public ResponseEntity<List<Category>> getAllCategories(){
        List<Category> allCategories = categoryService.getAllCategories();
        return new ResponseEntity<>(allCategories, HttpStatus.OK) ;
    }


    @PostMapping("api/admin/categories")
    public ResponseEntity<String> addCategory( @Valid @RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>("Category added successfully", HttpStatus.CREATED) ;
    }

    @DeleteMapping("api/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId){

            String status = categoryService.deleteCategory(categoryId);
            return  ResponseEntity.status(HttpStatus.OK).body(status);

    }

    @PutMapping("api/admin/categories/{categoryId}")
    public ResponseEntity<String> updateMapping( @Valid @RequestBody Category category, @PathVariable Long categoryId){

            Category updatedCategory = categoryService.updateCategory(category, categoryId);
            return new ResponseEntity<>("Category with category id" + categoryId, HttpStatus.OK);

    }
}
