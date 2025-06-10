package com.ecommerce.ashluxe.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "categories")

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @NotBlank
    @Size(min = 5, max = 50, message = "Category name must be between 2 and 50 characters")
    private String categoryName;
    @Version
    private Long version;

//    public Long getVersion() {
//        return version;
//    }
//
//    public void setVersion(Long version) {
//        this.version = version;
//    }

//
//    public Long getCategoryId() {
//        return categoryId;
//    }
//
//    public String getCategoryName() {
//        return categoryName;
//    }
//
//    public void setCategoryId(Long categoryId) {
//        this.categoryId = categoryId;
//    }
//
//    public void setCategoryName(String categoryName) {
//        this.categoryName = categoryName;
//    }
//
//    public Category(Long categoryId, String categoryName, Long version) {
//        this.categoryId = categoryId;
//        this.categoryName = categoryName;
//        this.version = version;
//    }
//    public Category() {
//        // No-arg constructor required by JPA
//    }
}
