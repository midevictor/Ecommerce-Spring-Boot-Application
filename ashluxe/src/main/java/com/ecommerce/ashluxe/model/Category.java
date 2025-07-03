package com.ecommerce.ashluxe.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;


}
