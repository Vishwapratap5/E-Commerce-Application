package com.guru.ecommerce.Service.Cart.Implementations;

import com.guru.ecommerce.DAO.CartItemRepository;
import com.guru.ecommerce.DAO.CartRepository;
import com.guru.ecommerce.DAO.ProductDAO;
import com.guru.ecommerce.Exceptions.APIException;
import com.guru.ecommerce.Exceptions.ResourceNotFoundException;
import com.guru.ecommerce.Model.Cart;
import com.guru.ecommerce.Model.CartItem;
import com.guru.ecommerce.Model.Product;
import com.guru.ecommerce.Payload.CartDTO;
import com.guru.ecommerce.Payload.ProductResponseDTO;
import com.guru.ecommerce.Service.Cart.CartService;
import com.guru.ecommerce.Utils.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CartServiceImpl implements CartService {

    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductDAO productRepository;
    @Autowired private AuthUtil authUtil;
    @Autowired private ModelMapper modelMapper;

    // ✅ ALWAYS SAFE
    private Cart getOrCreateCart() {
        return cartRepository.findByUser(authUtil.loggedInUser())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(authUtil.loggedInUser());
                    cart.setTotalPrice(0.0);
                    return cartRepository.save(cart);
                });
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {

        Cart cart = cartRepository.findCartByEmailAndCartId(emailId, cartId);

        if (cart == null) {
            throw new ResourceNotFoundException("Cart not found");
        }

        return mapCart(cart);
    }


    @Override
    @Transactional
    public void updateProductInCarts(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem item = cartItemRepository
                .findCartItemByProductIdAndCartId(productId, cartId);

        if (item == null) {
            throw new APIException("Product not in cart");
        }

        // update price
        item.setProductPrice(product.getSpecialPrice());
        item.setDiscount(product.getDiscount());

        cartItemRepository.save(item);

        // recalculate total
        cart.setTotalPrice(calculateTotal(cart));
        cartRepository.save(cart);
    }

    // ================= ADD =================
    @Override
    public CartDTO addProductToCart(Long productId, Integer quantity) {

        Cart cart = getOrCreateCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItem item = cartItemRepository
                .findCartItemByProductIdAndCartId(productId, cart.getId());

        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setProductPrice(product.getSpecialPrice());
            item.setDiscount(product.getDiscount());

            cart.getCartItems().add(item);
        }

        cartItemRepository.save(item);

        cart.setTotalPrice(calculateTotal(cart));
        cartRepository.save(cart);

        return mapCart(cart);
    }

    // ================= UPDATE =================
    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(Long productId, Integer sign) {

        Cart cart = getOrCreateCart();

        CartItem item = cartItemRepository
                .findCartItemByProductIdAndCartId(productId, cart.getId());

        if (item == null) throw new RuntimeException("Item not found");

        int newQty = item.getQuantity() + sign;

        if (newQty < 0) throw new RuntimeException("Invalid quantity");

        if (newQty == 0) {
            cart.getCartItems().remove(item);
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(newQty);
            cartItemRepository.save(item);
        }

        cart.setTotalPrice(calculateTotal(cart));
        cartRepository.save(cart);

        return mapCart(cart);
    }


    // ================= DELETE =================
    @Override
    @Transactional
    public String deleteProductFromCart(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cartItemRepository
                .findCartItemByProductIdAndCartId(productId, cartId);

        if (item == null) {
            throw new ResourceNotFoundException("Product not in cart");
        }

        // ✅ Only remove from collection
        cart.getCartItems().remove(item);

        // ❌ DO NOT call delete()

        cart.setTotalPrice(calculateTotal(cart));
        cartRepository.save(cart);

        return "Product removed";
    }

    // ================= GET MY CART =================
    @Override
    public CartDTO getCart() {
        Cart cart = getOrCreateCart();
        return mapCart(cart);
    }

    // ================= GET ALL =================
    @Override
    public List<CartDTO> getAllCarts() {
        return cartRepository.findAll()
                .stream()
                .map(this::mapCart)
                .toList();
    }

    // ================= MAPPER =================
    private CartDTO mapCart(Cart cart) {
        CartDTO dto = modelMapper.map(cart, CartDTO.class);

        List<ProductResponseDTO> products = cart.getCartItems().stream().map(item -> {
            ProductResponseDTO p = modelMapper.map(item.getProduct(), ProductResponseDTO.class);
            p.setQuantity(item.getQuantity());
            return p;
        }).toList();

        dto.setProducts(products);
        return dto;
    }

    private double calculateTotal(Cart cart) {
        return cart.getCartItems().stream()
                .mapToDouble(i -> i.getProductPrice() * i.getQuantity())
                .sum();
    }
}
