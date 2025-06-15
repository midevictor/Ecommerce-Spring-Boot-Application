package com.ecommerce.ashluxe.service;

import com.ecommerce.ashluxe.exceptions.APIException;
import com.ecommerce.ashluxe.exceptions.ResourceNotFoundException;
import com.ecommerce.ashluxe.model.Category;
import com.ecommerce.ashluxe.payload.CategoryDTO;
import com.ecommerce.ashluxe.payload.CategoryResponse;
import com.ecommerce.ashluxe.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> allCategories  = categoryPage.getContent();

        if(allCategories.isEmpty()){
            throw new APIException( "No categories found!!");
        }

        List<CategoryDTO> categoryDTOS = allCategories.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();


        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
//        category.setCategoryId(nextId++);
//        categories.add(category);
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDB != null) {
            throw new APIException( "Category with name " + category.getCategoryName() + " already exists!!");
        }
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
//        List<Category> categories = categoryRepository.findAll();
        Category savedcategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

        categoryRepository.delete(savedcategory);
        return modelMapper.map(savedcategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
//        List<Category> categories = categoryRepository.findAll();
        Optional<Category> savedCategoryOptional  = categoryRepository.findById(categoryId);
        Category savedCategory = savedCategoryOptional.orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));

//  //      Category category = modelMapper.map(categoryDTO, Category.class);
//  //      category.setCategoryId(categoryId);
// //       savedCategory = categoryRepository.save(category);
        // update the fields of the savedCategory with the values from categoryDTO
        savedCategory.setCategoryName(categoryDTO.getCategoryName());
        Category updatedCategory = categoryRepository.save(savedCategory);

//       //  savedCategory.setCategoryName(updatedCategory.getCategoryName());
//      //   savedCategory.setVersion(updatedCategory.getVersion());
        return modelMapper.map(updatedCategory, CategoryDTO.class);


    }
}
