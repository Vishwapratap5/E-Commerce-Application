package com.guru.ecommerce.Payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemDTO {

    private Long orderItemId;
    private ProductResponseDTO productResponseDTO;
    private Integer quantity;
    private double discount;
    private double orderedProductPrice;
    private double totalPrice;
}
