package com.guru.ecommerce.Service;

import com.guru.ecommerce.Payload.UserRegisterRequestDTO;
import com.guru.ecommerce.Payload.UserResponseDTO;

public interface UserAuthService {
     String getCurrentUserName();
    UserResponseDTO localCustomerRegistration(UserRegisterRequestDTO user);

    UserResponseDTO getCurrentUser();
}
