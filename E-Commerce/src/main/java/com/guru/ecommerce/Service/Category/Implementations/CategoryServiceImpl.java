package com.guru.ecommerce.Service.Category.Implementations;

import com.guru.ecommerce.DAO.CategoryDAO;
import com.guru.ecommerce.Exceptions.CategoryDuplicationException;
import com.guru.ecommerce.Exceptions.CategoryNotFoundException;
import com.guru.ecommerce.Model.Category;
import com.guru.ecommerce.Service.Category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private CategoryDAO categoryDAO;
    @Autowired
    public CategoryServiceImpl(CategoryDAO categoryDAO) {
        this.categoryDAO=categoryDAO;
    }


    @Override
    public List<Category> getAllCategories() {

        List<Category> categories=categoryDAO.findAll();
        if(categories.isEmpty())
        {
            throw new CategoryNotFoundException("There is no Category created");
        }
        return categories;
    }

    @Override
    public Category createCategory(Category category) {
        if(categoryDAO.existsByCategoryNameIgnoreCase(category.getCategoryName())){
            throw new CategoryDuplicationException("category with"+category.getCategoryName()+"already exists");
        }

            return categoryDAO.save(category);
    }

    @Override
    public Category deleteCategory(Long id) {
        Category category=categoryDAO.findById(id).orElseThrow(()->new CategoryNotFoundException("Category not found"));
        categoryDAO.delete(category);
        return category;
    }

    @Override
    public Category updateCategory(Long id, Category category) {

        if(categoryDAO.existsByCategoryNameIgnoreCase(category.getCategoryName())){
            throw new CategoryDuplicationException("category with "+category.getCategoryName()+" already exists");
        }

        Category newCategory=categoryDAO.findById(id).orElseThrow(()-> new CategoryNotFoundException("Category not found"));

            newCategory.setCategoryName(category.getCategoryName());
            return categoryDAO.save(newCategory);
    }
}
