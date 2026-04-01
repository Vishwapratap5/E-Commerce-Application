package com.guru.ecommerce.Exceptions;

public class APIException extends RuntimeException
{
    public APIException(String message)
    {
        super(message);
    }
}
