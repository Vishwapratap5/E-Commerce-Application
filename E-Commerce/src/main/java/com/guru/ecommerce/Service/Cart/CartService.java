package com.guru.ecommerce.Service.Cart;

import com.guru.ecommerce.Payload.CartDTO;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {
//    CartDTO addProductToCart(Long productId, Integer quantity);
//
//    List<CartDTO> getAllCarts();
//
//    CartDTO getCartsById(String email, Long id);
//
//    CartDTO getCart(String emailId, Long cartId);
//
//    @Transactional
//    CartDTO updateProductQuantityInCart(Long productId, Integer sign);
//
//    String deleteProductFromCart(Long productId);
//
//    @Transactional
//    String deleteProductFromCart(Long cartId, Long productId);
//
//    void updateProductInCarts(Long cartId, Long productId);

    CartDTO addProductToCart(Long productId, Integer quantity);

    // ================= GET MY CART =================
    CartDTO getCart();

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long cartId);

    @Transactional
    CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);

    void updateProductInCarts(Long cartId, Long productId);
}
