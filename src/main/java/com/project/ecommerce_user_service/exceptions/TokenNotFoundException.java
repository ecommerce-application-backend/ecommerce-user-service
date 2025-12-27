package com.project.ecommerce_user_service.exceptions;

public class TokenNotFoundException extends Exception{
    public TokenNotFoundException(String message) {
        super(message);
    }
}
