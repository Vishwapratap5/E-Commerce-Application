package com.guru.ecommerce.Service.Products.Implementations;

import com.guru.ecommerce.DAO.CategoryDAO;
import com.guru.ecommerce.DAO.ProductDAO;
import com.guru.ecommerce.Exceptions.ResourceDuplicationException;
import com.guru.ecommerce.Exceptions.ResourceNotFoundException;
import com.guru.ecommerce.Model.Category;
import com.guru.ecommerce.Model.Product;
import com.guru.ecommerce.Payload.ProductListResponseDTO;
import com.guru.ecommerce.Payload.ProductRequestDTO;
import com.guru.ecommerce.Payload.ProductResponseDTO;
import com.guru.ecommerce.Service.FileService.FileService;
import com.guru.ecommerce.Service.Products.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

   @Autowired
    private ProductDAO productDAO;

   @Autowired
   private CategoryDAO categoryDAO;

   @Autowired
   private ModelMapper modelMapper;

   @Autowired
   private FileService fileService;

   @Value("${file.upload-dir}")
   private String path;



    @Override
    public ProductResponseDTO addProduct(Long categoryId, ProductRequestDTO productRequestDTO) {
        Category category=categoryDAO.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category with id:"+categoryId+" not found"));


        List<Product> products=category.getProduct();
        boolean exists=false;
        for(Product product:products){
            if(product.getProductName().equalsIgnoreCase(productRequestDTO.getProductName())){
               exists=true;
               break;
            }
        }

        if(!exists){
            Product product=modelMapper.map(productRequestDTO,Product.class);


            double specialPrice=productRequestDTO.getPrice()-(productRequestDTO.getPrice()*(productRequestDTO.getDiscount()/100));
            product.setProductId(null);
            product.setCategory(category);
            product.setImage(productRequestDTO.getImage());
            product.setSpecialPrice(specialPrice);

            Product savedProduct=productDAO.save(product);

            return  ProductResponseDTO.builder()
                    .productId(savedProduct.getProductId())
                    .productName(savedProduct.getProductName())
                    .image(savedProduct.getImage())
                    .price(savedProduct.getPrice())
                    .specialPrice(savedProduct.getSpecialPrice())
                    .categoryName(savedProduct.getCategory().getCategoryName())
                    .build();
        }else{
            throw new ResourceDuplicationException("this resource already exists");
        }

    }



    @Override
    public ProductListResponseDTO getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrders = sortOrder.equalsIgnoreCase("asc")
                                        ?Sort.by(sortBy).ascending()
                                        :Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sortByAndOrders);
        Page<Product> pageProducts=productDAO.findAll(pageable);
        List<Product> products=pageProducts.getContent();
        if(products.isEmpty()){
            throw new ResourceNotFoundException("Product not found");
        }
        List<ProductResponseDTO> productList=products.stream().map(product->modelMapper.map(product,ProductResponseDTO.class)).toList();

        ProductListResponseDTO productListResponseDTO=new ProductListResponseDTO();
        productListResponseDTO.setProducts(productList);
        productListResponseDTO.setTotalPages(pageProducts.getTotalPages());
        productListResponseDTO.setPageNumber(pageNumber);
        productListResponseDTO.setPageSize(pageSize);
        productListResponseDTO.setHasNext(pageProducts.hasNext());
        productListResponseDTO.setHasPrevious(pageProducts.hasPrevious());
        productListResponseDTO.setTotal(pageProducts.getTotalElements());
        productListResponseDTO.setIsSorted(pageProducts.getSort().isSorted());
        return productListResponseDTO;
    }



    @Override
    public ProductListResponseDTO getAllProductsByCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,Long categoryId) {

        Sort sortByAndOrders = sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sortByAndOrders);

        Category category=categoryDAO.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category with id:"+categoryId+" not found"));

        Page<Product> pageProducts=productDAO.findByCategoryOrderByPriceAsc(category,pageable);
        List<Product> products=pageProducts.getContent();
        if(products.isEmpty()){
            throw new ResourceNotFoundException("Product not found for category with id:"+categoryId);
        }

        ProductListResponseDTO productListResponseDTO=new ProductListResponseDTO();

        List<ProductResponseDTO> productList=products.stream().map(product->modelMapper.map(product,ProductResponseDTO.class)).toList();

        productListResponseDTO.setProducts(productList);
        productListResponseDTO.setTotalPages(pageProducts.getTotalPages());
        productListResponseDTO.setPageNumber(pageNumber);
        productListResponseDTO.setPageSize(pageSize);
        productListResponseDTO.setHasNext(pageProducts.hasNext());
        productListResponseDTO.setHasPrevious(pageProducts.hasPrevious());
        productListResponseDTO.setTotal(pageProducts.getTotalElements());
        productListResponseDTO.setIsSorted(pageProducts.getSort().isSorted());
        return productListResponseDTO;
    }



    @Override
    public ProductListResponseDTO getProductByKeyword(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,String keyword) {

        Sort sortByAndOrders = sortOrder.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        Pageable pageable= PageRequest.of(pageNumber,pageSize,sortByAndOrders);

        Page<Product> pageProducts=productDAO.findByProductNameLikeIgnoreCase("%"+keyword+"%",pageable);
        List<Product> products=pageProducts.getContent();
        if(products.isEmpty()){
            throw new ResourceNotFoundException("Product not found for keyword:"+keyword);
        }
        ProductListResponseDTO productListResponseDTO=new ProductListResponseDTO();

        List<ProductResponseDTO> productList=products.stream().map(product->modelMapper.map(product,ProductResponseDTO.class)).toList();

        productListResponseDTO.setProducts(productList);
        productListResponseDTO.setTotalPages(pageProducts.getTotalPages());
        productListResponseDTO.setPageNumber(pageNumber);
        productListResponseDTO.setPageSize(pageSize);
        productListResponseDTO.setHasNext(pageProducts.hasNext());
        productListResponseDTO.setHasPrevious(pageProducts.hasPrevious());
        productListResponseDTO.setTotal(pageProducts.getTotalElements());
        productListResponseDTO.setIsSorted(pageProducts.getSort().isSorted());
        return productListResponseDTO;
    }



    @Override
    public ProductResponseDTO updateProduct(Long productId,ProductRequestDTO productRequestDTO) {
        Product productFromDb=productDAO.findById(productId)
                                        .orElseThrow(()->new ResourceNotFoundException("Product with id:"+productId+" not found"));

        productFromDb.setProductName(productRequestDTO.getProductName());
        productFromDb.setPrice(productRequestDTO.getPrice());
        productFromDb.setDiscount(productRequestDTO.getDiscount());
        productFromDb.setDescription(productRequestDTO.getDescription());

        double specialPrice=productRequestDTO.getPrice()-(productRequestDTO.getPrice()*(productRequestDTO.getDiscount()/100));
        productFromDb.setSpecialPrice(specialPrice);

        Product savedProduct=productDAO.save(productFromDb);

        return modelMapper.map(savedProduct,ProductResponseDTO.class);
    }



    @Override
    public ProductResponseDTO deleteProduct(Long productId) {

        Product product=productDAO.findById(productId)
                                  .orElseThrow(()->new ResourceNotFoundException("Product with id:"+productId+" not found"));
       productDAO.delete(product);
        return modelMapper.map(product,ProductResponseDTO.class);
    }



    @Override
    public ProductResponseDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDb=productDAO.findById(productId)
                                  .orElseThrow(()->new ResourceNotFoundException("Product with id:"+productId+" not found"));



        String fileName=fileService.uploadImage(path,image);

        productFromDb.setImage(fileName);

        Product updatedProduct=productDAO.save(productFromDb);
        return modelMapper.map(updatedProduct,ProductResponseDTO.class);
    }


}
