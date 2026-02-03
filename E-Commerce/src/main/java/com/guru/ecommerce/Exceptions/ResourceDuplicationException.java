package com.guru.ecommerce.Exceptions;

public class ResourceDuplicationException extends RuntimeException{
    public ResourceDuplicationException(String message){
        super(message);
    }
}
