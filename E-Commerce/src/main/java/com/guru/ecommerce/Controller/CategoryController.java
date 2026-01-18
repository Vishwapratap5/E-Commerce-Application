package com.guru.ecommerce.Controller;

import com.guru.ecommerce.Configurations.AppConstants;
import com.guru.ecommerce.Model.Category;
import com.guru.ecommerce.Payload.CategoryListResponseDTO;
import com.guru.ecommerce.Payload.CategoryRequestDTO;
import com.guru.ecommerce.Payload.CreateCategoryRequestDTO;
import com.guru.ecommerce.Payload.CategoryResponseDTO;
import com.guru.ecommerce.Service.Category.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    @GetMapping("public/categories")
    public ResponseEntity<CategoryListResponseDTO> getAllCategory(@RequestParam(name="pageSize" , required = false,defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                  @RequestParam(name="pageNumber",required = false,defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
                                                                  @RequestParam(name="sortBy",required = false,defaultValue = AppConstants.SORT_BY) String sortBy,
                                                                  @RequestParam(name="sortOrder",required = false,defaultValue =AppConstants.SORT_ORDER) String sortOrder){

        CategoryListResponseDTO categoryResponse =categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);

    }

    @PostMapping("/public/addcategories")
    public ResponseEntity<CategoryResponseDTO> addCategory(@Valid @RequestBody CreateCategoryRequestDTO category){

        CategoryResponseDTO newCategory= categoryService.createCategory(category);
        return new ResponseEntity<>(newCategory, HttpStatus.CREATED);

    }


    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<CategoryResponseDTO> deleteCategory(@PathVariable("id") Long id){

        CategoryResponseDTO Category= categoryService.deleteCategory(id);
        return new ResponseEntity<>(Category, HttpStatus.OK);

    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@Valid @PathVariable("id") Long id, @RequestBody CategoryRequestDTO category){

        CategoryResponseDTO updatedCategory=categoryService.updateCategory(id,category);
        return new ResponseEntity<>(updatedCategory, HttpStatus.OK);

    }

}
