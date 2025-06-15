package com.ecommerce.ashluxe.service;

import com.ecommerce.ashluxe.model.Category;
import com.ecommerce.ashluxe.payload.CategoryDTO;
import com.ecommerce.ashluxe.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
//    List<Category> getAllCategories();
    CategoryResponse getAllCategories();
//    void createCategory(Category category);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryId);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);


}
