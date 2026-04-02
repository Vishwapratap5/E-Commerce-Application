package com.guru.ecommerce.Controller;

import com.guru.ecommerce.Model.Order;
import com.guru.ecommerce.Payload.OrderDTO;
import com.guru.ecommerce.Payload.OrderRequestDTO;
import com.guru.ecommerce.Service.Order.OrderService;
import com.guru.ecommerce.Utils.AuthUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> orderProducts(@PathVariable("paymentMethod") String paymentMethod, @RequestBody OrderRequestDTO orderRequestDTO) {
       String emailId=authUtil.loggedInEmail();
       OrderDTO placedOrder= orderService.placeOrder(
                emailId,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );
       return new ResponseEntity<>(placedOrder, HttpStatus.OK);
    }
}

/* we are not taking the product information because here checkout is the next page
 * we are converting the cart into order and if user want to remove/add the product
 * then he can do so from cart(add/delete product from cart)*/
