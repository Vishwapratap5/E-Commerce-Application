package com.guru.ecommerce.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> CategoryNotFoundException(CategoryNotFoundException e, WebRequest webRequest) {
        ErrorResponse resp = new ErrorResponse();
        resp.timestamp = LocalDateTime.now();
        resp.errorMessage = e.getMessage();
        resp.errorCode=HttpStatus.NOT_FOUND;
        resp.errorDetail = "Category not found";

        return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResonse> handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        ValidationErrorResonse error = new ValidationErrorResonse();
        error.timestamp = LocalDateTime.now();
        error.errorCode = HttpStatus.BAD_REQUEST;
        error.errorMessage = "Validation error";
        error.errors = fieldErrors;
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryDuplicationException.class)
    public ResponseEntity<ErrorResponse> handleException(CategoryDuplicationException e, WebRequest webRequest) {
        ErrorResponse resp = new ErrorResponse();
        resp.timestamp = LocalDateTime.now();
        resp.errorMessage = e.getMessage();
        resp.errorCode = HttpStatus.BAD_REQUEST;
        resp.errorDetail = "Category duplication";
        return new ResponseEntity<>(resp, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(ResourceNotFoundException e, WebRequest webRequest) {
        ErrorResponse resp = new ErrorResponse();
        resp.timestamp = LocalDateTime.now();
        resp.errorMessage = e.getMessage();
        resp.errorCode = HttpStatus.NOT_FOUND;
        resp.errorDetail = "Given Resource not found";
        return new ResponseEntity<>(resp, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceDuplicationException.class)
    public ResponseEntity<ErrorResponse> ResourceDuplicationException(ResourceDuplicationException e, WebRequest webRequest) {
        ErrorResponse resp = new ErrorResponse();
        resp.timestamp = LocalDateTime.now();
        resp.errorMessage = e.getMessage();
        resp.errorCode = HttpStatus.CONFLICT;
        resp.errorDetail = "Given Resource Already Exists";
        return new ResponseEntity<>(resp, HttpStatus.CONFLICT);
    }

}
