package com.guru.ecommerce.Service.Products;

import com.guru.ecommerce.Payload.ProductListResponseDTO;
import com.guru.ecommerce.Payload.ProductRequestDTO;
import com.guru.ecommerce.Payload.ProductResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ProductResponseDTO addProduct(Long categoryId, ProductRequestDTO productRequestDTO);

    ProductListResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductListResponseDTO getAllProductsByCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,Long categoryId);

    ProductListResponseDTO getProductByKeyword(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,String keyword);

    ProductResponseDTO updateProduct(Long productId,ProductRequestDTO productRequestDTO);

    ProductResponseDTO deleteProduct(Long productId);

    ProductResponseDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
