package com.guru.ecommerce.DAO;

import com.guru.ecommerce.Model.Category;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryDAO extends JpaRepository<Category,Long> {

    boolean existsByCategoryNameIgnoreCase(String categoryName);
}
