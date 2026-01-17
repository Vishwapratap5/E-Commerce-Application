package com.guru.ecommerce.Exceptions;

public class CategoryDuplicationException extends RuntimeException
{
    public CategoryDuplicationException(String message)
    {
        super(message);
    }
}
