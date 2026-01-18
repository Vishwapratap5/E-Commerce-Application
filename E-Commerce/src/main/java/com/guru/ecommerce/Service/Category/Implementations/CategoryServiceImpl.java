package com.guru.ecommerce.Service.Category.Implementations;

import com.guru.ecommerce.Configurations.AppConstants;
import com.guru.ecommerce.DAO.CategoryDAO;
import com.guru.ecommerce.Exceptions.CategoryDuplicationException;
import com.guru.ecommerce.Exceptions.CategoryNotFoundException;
import com.guru.ecommerce.Model.Category;
import com.guru.ecommerce.Payload.CategoryRequestDTO;
import com.guru.ecommerce.Payload.CategoryListResponseDTO;
import com.guru.ecommerce.Payload.CreateCategoryRequestDTO;
import com.guru.ecommerce.Payload.CategoryResponseDTO;
import com.guru.ecommerce.Service.Category.CategoryService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private CategoryDAO categoryDAO;
    private ModelMapper modelMapper;


    @Override
    public CategoryListResponseDTO getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

        Sort sort=sortOrder.equalsIgnoreCase("asc")?
                             Sort.by(sortBy).ascending()
                            :Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);

        Page<Category> categories=categoryDAO.findAll(pageable);
        List<Category> categoriesList=categories.getContent();
        if(categoriesList.isEmpty())
        {
            throw new CategoryNotFoundException("There is no Category created");
        }
        List<CategoryResponseDTO> List=categories.stream()
                                                 .map(c->modelMapper.map(c, CategoryResponseDTO.class)).toList();
        CategoryListResponseDTO categoryResponseDTO=new CategoryListResponseDTO();
        categoryResponseDTO.setCategories(List);
        categoryResponseDTO.setTotalPages(categories.getTotalPages());
        categoryResponseDTO.setPageSize(categories.getPageable().getPageSize());
        categoryResponseDTO.setPageNumber(categories.getPageable().getPageNumber());
        categoryResponseDTO.setHasPrevious(categories.hasPrevious());
        categoryResponseDTO.setHasNext(categories.hasNext());
        categoryResponseDTO.setTotal(categories.getTotalElements());
        categoryResponseDTO.setIsSorted(categories.getSort().isSorted());
        return categoryResponseDTO;
    }

    @Override
    public CategoryResponseDTO createCategory(CreateCategoryRequestDTO category) {

        Category newCategory=modelMapper.map(category,Category.class);
        if(categoryDAO.existsByCategoryNameIgnoreCase(category.getCategoryName())){
            throw new CategoryDuplicationException("category with"+category.getCategoryName()+"already exists");
        }

        Category savedCategory=categoryDAO.save(newCategory);
        return modelMapper.map(savedCategory, CategoryResponseDTO.class);
    }

    @Override
    public CategoryResponseDTO deleteCategory(Long id) {
        Category category=categoryDAO.findById(id).orElseThrow(()->new CategoryNotFoundException("Category not found"));
        categoryDAO.delete(category);
       return modelMapper.map(category, CategoryResponseDTO.class);
    }

    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO category) {

        Category newCategory=modelMapper.map(category,Category.class);

        if(categoryDAO.existsByCategoryNameIgnoreCase(newCategory.getCategoryName())){
            throw new CategoryDuplicationException("category with "+newCategory.getCategoryName()+" already exists");
        }

        Category foundCategory=categoryDAO.findById(id).orElseThrow(()-> new CategoryNotFoundException("Category not found"));

            foundCategory.setCategoryName(newCategory.getCategoryName());
            categoryDAO.save(foundCategory);
            return modelMapper.map(foundCategory, CategoryResponseDTO.class);
    }
}
