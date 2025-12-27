package com.project.ecommerce.user_service.exceptions;

public class TokenNotFoundException extends Exception{
    public TokenNotFoundException(String message) {
        super(message);
    }
}
