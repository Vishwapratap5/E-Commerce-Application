package com.guru.ecommerce.Service.Order.Implementations;

import com.guru.ecommerce.DAO.*;
import com.guru.ecommerce.Exceptions.APIException;
import com.guru.ecommerce.Exceptions.ResourceNotFoundException;
import com.guru.ecommerce.Model.*;
import com.guru.ecommerce.Payload.OrderDTO;
import com.guru.ecommerce.Payload.OrderItemDTO;
import com.guru.ecommerce.Service.Cart.CartService;
import com.guru.ecommerce.Service.Order.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ProductDAO productDAO;
    @Autowired
    private CartService cartService;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage) {
        /*getting user cart
        * create a new order with paymentInfo
        * get items from the cart into the order items
        * Update product stock
        * clear the cart
        * send back order summary*/

        Cart cart=cartRepository.findCartByEmail(emailId);

        if(cart==null){
            throw new ResourceNotFoundException("cart not found for user: "+emailId);
        }

        Address address=addressRepository.findById(addressId).orElseThrow(()->new ResourceNotFoundException("address not found for user: "+emailId));
        Order order=new Order();
        order.setEmail(emailId);
        order.setAddress(address);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Order accepted..!");

        Payment payment=new Payment(paymentMethod,pgPaymentId,pgStatus,pgResponseMessage,pgName);
        payment.setOrder(order);
        payment=paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder=orderRepository.save(order);

        List<CartItem> cartItems=cart.getCartItems();
        if(cartItems.isEmpty()){
            throw new APIException("cart is empty");
        }

        List<OrderItem> orderItems=new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem=new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setOrderedProductPrice(cartItem.getProductPrice());
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
        }

        orderItems=orderItemRepository.saveAll(orderItems);

        List<CartItem> cartItemsCopy = new ArrayList<>(cart.getCartItems());

        for (CartItem cartItem : cartItemsCopy) {

            int quantity = cartItem.getQuantity();
            Product product = cartItem.getProduct();

            product.setQuantity(product.getQuantity() - quantity);
            productDAO.save(product);

            cartService.deleteProductFromCart(cart.getId(), cartItem.getProduct().getProductId());
        }


        OrderDTO orderDTO=modelMapper.map(savedOrder,OrderDTO.class);
        orderItems.forEach(orderItem->orderDTO.getOrderItemList().add(modelMapper.map(orderItem, OrderItemDTO.class)));
        orderDTO.setAddressId(addressId);
        return orderDTO;
    }
}
