package com.project.ecommerce_user_service.exceptions;

public class UserSessionExceededException extends Exception{
    public UserSessionExceededException(String message) {
        super(message);
    }
}
