package com.project.ecommerce_user_service.exceptions;

public class InvalidTokenException extends Exception{
    public InvalidTokenException(String message) {
        super(message);
    }
}
