package com.guru.ecommerce.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemDTO {
    private Long cartItemId;
    private CartDTO cart;
    private ProductResponseDTO product;
    private Integer quantity;
    private Double discount;
    private Double productPrice;
}
