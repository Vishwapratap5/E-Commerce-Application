package com.guru.ecommerce.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ValidationErrorResonse {
    public LocalDateTime timestamp;
    public String errorMessage;
    public HttpStatus errorCode;
    public Map<String,String> errors;
}
