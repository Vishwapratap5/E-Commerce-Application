package com.guru.ecommerce.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductListResponseDTO {

    private List<ProductResponseDTO> products;
    Integer pageNumber;
    Integer pageSize;
    Boolean hasNext;
    Integer totalPages;
    Boolean hasPrevious;
    Long total;
    Boolean isSorted;

}
