package com.ecommerce.ashluxe.service;

import com.ecommerce.ashluxe.exceptions.APIException;
import com.ecommerce.ashluxe.exceptions.ResourceNotFoundException;
import com.ecommerce.ashluxe.model.Category;
import com.ecommerce.ashluxe.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {
   // private List<Category> categories = new ArrayList<>();
//    private Long nextId = 1L;
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> allCategories  = categoryRepository.findAll();
        if(allCategories.isEmpty()){
            throw new APIException( "No categories found!!");
        }
        return allCategories;
    }

    @Override
    public void createCategory(Category category) {
//        category.setCategoryId(nextId++);
//        categories.add(category);
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null) {
            throw new APIException( "Category with name " + category.getCategoryName() + " already exists!!");
        }
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
//        List<Category> categories = categoryRepository.findAll();
        Category savedcategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        categoryRepository.delete(savedcategory);
        return "Category with categoryId: " + categoryId + " is deleted";
    }

    @Override
    public Category updateCategory(Category updatedCategory, Long categoryId) {
//        List<Category> categories = categoryRepository.findAll();
        Optional<Category> savedCategoryOptional  = categoryRepository.findById(categoryId);
        Category savedCategory = savedCategoryOptional.orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        savedCategory.setCategoryName(updatedCategory.getCategoryName());
        savedCategory.setVersion(updatedCategory.getVersion());
        return categoryRepository.save(savedCategory);


    }
}
