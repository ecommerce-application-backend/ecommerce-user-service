package com.project.ecommerce.user_service.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.ecommerce.user_service.exceptions.*;
import com.project.ecommerce.user_service.models.Token;
import com.project.ecommerce.user_service.models.User;

public interface UserService {
    User signUp(String name, String email, String password) throws UserAlreadyExistsException, JsonProcessingException;

    Token login(String email, String password) throws UserNotFoundException, UserSessionExceededException;

    void logout(String token) throws TokenNotFoundException;

    User validateToken(String token) throws InvalidTokenException;
}
