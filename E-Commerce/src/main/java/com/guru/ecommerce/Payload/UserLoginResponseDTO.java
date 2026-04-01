package com.guru.ecommerce.Payload;

public record UserLoginResponseDTO(String token,
                                   String type,
                                   long expiresIn) {
}
