package com.guru.ecommerce.Service.Category;

import com.guru.ecommerce.Model.Category;

import java.util.List;


public interface CategoryService {
    List<Category> getAllCategories();
    Category createCategory(Category category);

    Category deleteCategory(Long id);

    Category updateCategory(Long id, Category category);
}
