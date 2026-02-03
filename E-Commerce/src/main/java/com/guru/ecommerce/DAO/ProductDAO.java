package com.guru.ecommerce.DAO;

import com.guru.ecommerce.Model.Category;
import com.guru.ecommerce.Model.Product;
import com.guru.ecommerce.Payload.ProductListResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDAO extends JpaRepository<Product,Long> {
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageable);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword,Pageable pageable);
}
