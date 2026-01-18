package com.guru.ecommerce.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryListResponseDTO {
    List<CategoryResponseDTO> categories;
    Integer pageNumber;
    Integer pageSize;
    Boolean hasNext;
    Integer totalPages;
    Boolean hasPrevious;
    Long total;
    Boolean isSorted;

}
