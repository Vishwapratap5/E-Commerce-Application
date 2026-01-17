package com.guru.ecommerce.Controller;

import com.guru.ecommerce.Model.Category;
import com.guru.ecommerce.Service.Category.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @GetMapping("public/categories")
    public ResponseEntity<List<Category>> getAllCategory(){

        List<Category> categoryArrayList =categoryService.getAllCategories();
        return new ResponseEntity<>(categoryArrayList, HttpStatus.OK);

    }

    @PostMapping("/public/addcategories")
    public ResponseEntity<Category> addCategory(@Valid @RequestBody Category category){

        Category newCategory= categoryService.createCategory(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);

    }


    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable("id") Long id){

        Category Category= categoryService.deleteCategory(id);
        return new ResponseEntity<>(Category, HttpStatus.OK);

    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Category> updateCategory(@Valid @PathVariable("id") Long id, @RequestBody Category category){

        Category updatedCategory=categoryService.updateCategory(id,category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);

    }

}
