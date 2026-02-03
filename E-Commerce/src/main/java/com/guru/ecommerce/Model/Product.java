package com.guru.ecommerce.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank
    @Size(min = 3, message = "Product Name Must Be At Least 3 Characters")
    private String productName;


    private String image;

    @NotBlank
    @Size(min = 6, message = "Product Description Must Be At Least 6 Characters")
    private String description;


    private double price;
    private double specialPrice;
    private Integer quantity;
    private double discount;


    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;


}
