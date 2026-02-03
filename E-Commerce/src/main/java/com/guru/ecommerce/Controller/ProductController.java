package com.guru.ecommerce.Controller;

import com.guru.ecommerce.Configurations.AppConstants;
import com.guru.ecommerce.Payload.ProductListResponseDTO;
import com.guru.ecommerce.Payload.ProductRequestDTO;
import com.guru.ecommerce.Payload.ProductResponseDTO;
import com.guru.ecommerce.Service.Products.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }



    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductResponseDTO> addProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO,
                                                         @PathVariable("categoryId") Long categoryId) {
        ProductResponseDTO product= productService.addProduct(categoryId,productRequestDTO);
        return ResponseEntity.ok().body(product);

    }


    @GetMapping("/public/products")
    public ResponseEntity<ProductListResponseDTO> getAllProducts(@RequestParam(name ="pageNumber",defaultValue = AppConstants.PAGE_NUMBER)Integer pageNumber,
                                                                 @RequestParam(name ="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                 @RequestParam(name ="sortBy",defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
                                                                 @RequestParam(name ="sortOrder",defaultValue = AppConstants.SORT_ORDER) String sortOrder) {
        ProductListResponseDTO productList=productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
        return ResponseEntity.ok().body(productList);
    }



    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductListResponseDTO> getAllProductsByCategory(@RequestParam(name ="pageNumber",defaultValue = AppConstants.PAGE_NUMBER)Integer pageNumber,
                                                                           @RequestParam(name ="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                           @RequestParam(name ="sortBy",defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
                                                                           @RequestParam(name ="sortOrder",defaultValue = AppConstants.SORT_ORDER) String sortOrder,
                                                                           @PathVariable("categoryId") Long categoryId) {
        ProductListResponseDTO productList=productService.getAllProductsByCategory(pageNumber,pageSize,sortBy,sortOrder,categoryId);
        return ResponseEntity.ok().body(productList);
    }




    @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductListResponseDTO> getAllProductsByKeyword(@RequestParam(name ="pageNumber",defaultValue = AppConstants.PAGE_NUMBER)Integer pageNumber,
                                                                          @RequestParam(name ="pageSize",defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
                                                                          @RequestParam(name ="sortBy",defaultValue = AppConstants.SORT_PRODUCT_BY) String sortBy,
                                                                          @RequestParam(name ="sortOrder",defaultValue = AppConstants.SORT_ORDER) String sortOrder,
                                                                          @PathVariable("keyword") String keyword) {
        ProductListResponseDTO productListResponseDTO=productService.getProductByKeyword(pageNumber,pageSize,sortBy,sortOrder,keyword);
        return ResponseEntity.ok().body(productListResponseDTO);
    }



    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable("productId") Long productId,@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO productResponseDTO=productService.updateProduct(productId,productRequestDTO);
        return ResponseEntity.ok().body(productResponseDTO);

    }



    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductResponseDTO> deleteProduct(@PathVariable("productId") Long productId) {
        ProductResponseDTO productResponseDTO=productService.deleteProduct(productId);
        return ResponseEntity.ok().body(productResponseDTO);
    }



    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductResponseDTO> updateProductImage(@PathVariable("productId") Long productId,
                                                                 @RequestParam("image")MultipartFile image) throws IOException {
        ProductResponseDTO updatedProduct=productService.updateProductImage(productId,image);
        return  ResponseEntity.ok().body(updatedProduct);
    }
}
