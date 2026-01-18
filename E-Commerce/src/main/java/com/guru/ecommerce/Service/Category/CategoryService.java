package com.guru.ecommerce.Service.Category;

import com.guru.ecommerce.Model.Category;
import com.guru.ecommerce.Payload.CategoryListResponseDTO;
import com.guru.ecommerce.Payload.CategoryRequestDTO;
import com.guru.ecommerce.Payload.CreateCategoryRequestDTO;
import com.guru.ecommerce.Payload.CategoryResponseDTO;


public interface CategoryService {
    CategoryListResponseDTO getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    CategoryResponseDTO createCategory(CreateCategoryRequestDTO category);

    CategoryResponseDTO deleteCategory(Long id);

    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO category);
}
