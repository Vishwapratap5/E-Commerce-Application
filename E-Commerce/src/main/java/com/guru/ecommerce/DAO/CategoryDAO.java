package com.guru.ecommerce.DAO;

import com.guru.ecommerce.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDAO extends JpaRepository<Category,Long> {

    boolean existsByCategoryNameIgnoreCase(String categoryName);


}
