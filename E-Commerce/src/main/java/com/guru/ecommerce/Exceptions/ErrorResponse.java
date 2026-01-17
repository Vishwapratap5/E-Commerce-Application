package com.guru.ecommerce.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {
    public LocalDateTime timestamp;
    public String errorMessage;
    public HttpStatus errorCode;
    public String errorDetail;
}
