package com.project.ecommerce_user_service.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ecommerce_user_service.configurations.KafkaProducerClient;
import com.project.ecommerce_user_service.dtos.SendEmailDto;
import com.project.ecommerce_user_service.exceptions.*;
import com.project.ecommerce_user_service.models.Token;
import com.project.ecommerce_user_service.models.User;
import com.project.ecommerce_user_service.repositories.TokenRepository;
import com.project.ecommerce_user_service.repositories.UserRepository;
import com.project.ecommerce_user_service.services.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private TokenRepository tokenRepository;
    private KafkaProducerClient kafkaProducerClient;
    private ObjectMapper objectMapper;

    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           TokenRepository tokenRepository,
                           KafkaProducerClient kafkaProducerClient,
                           ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.tokenRepository = tokenRepository;
        this.kafkaProducerClient = kafkaProducerClient;
        this.objectMapper = objectMapper;
    }
    @Override
    public User signUp(String name, String email, String password) throws UserAlreadyExistsException, JsonProcessingException {
        // Verify if user already exists
        Optional<User> optionalUser = this.userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        // Storing Hashed + Salted password in DB
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setEmailVerified(Boolean.TRUE);

        user = userRepository.save(user);

        /*
        Once a new account is created for the user, we need to send event to the Kafka topic
        so that EmailService can consume it
         */

        SendEmailDto sendEmailDto = new SendEmailDto();
        sendEmailDto.setTo(email);
        sendEmailDto.setFrom("admin@amazon.com");
        sendEmailDto.setSubject("Welcome to Amazon");
        sendEmailDto.setBody("Thanks for joining Amazon");

        this.kafkaProducerClient.sendMessage(
                "sendEmail",
                objectMapper.writeValueAsString(sendEmailDto)
        );

        return user;
    }

    @Override
    public Token login(String email, String password) throws UserNotFoundException, UserSessionExceededException {
        // Verify if user exists or not
        Optional<User> optionalUser = this.userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("Invalid credentials");
        }

        User user = optionalUser.get();
        String hashedPassword = user.getHashedPassword();
        // password doesn't match
        if (!bCryptPasswordEncoder.matches(password, hashedPassword)) {
            throw new UserNotFoundException("Invalid credentials");
        }

        // User shouldn't be allowed to have more than 2 sessions at once
        List<Token> tokens = this.tokenRepository.findAllByUser_IdAndIsDeletedFalse(user.getId());
        if (tokens.size() > 1) {
            throw new UserSessionExceededException("User already has 2 active sessions");
        }

        // Generate a new token
        Token token = generateToken(user);
        return tokenRepository.save(token);
    }

    private Token generateToken(User user) {
        Token token = new Token();

        LocalDateTime expiryTime = LocalDateTime.now().plusDays(30);
        token.setExpiryAt(expiryTime);

        String value = RandomStringUtils.randomAlphanumeric(128);
        token.setValue(value);

        token.setUser(user);

        return token;
    }

    @Override
    public void logout(String tokenValue) throws TokenNotFoundException {
        Optional<Token> optionalToken = this.tokenRepository.findByValueAndIsDeletedFalse(tokenValue);
        if (optionalToken.isEmpty()) {
            throw new TokenNotFoundException("Token doesn't exist");
        }

        Token token = optionalToken.get();
        token.setDeleted(Boolean.TRUE);

        this.tokenRepository.save(token);
    }

    @Override
    public User validateToken(String tokenValue) throws InvalidTokenException {
        Optional<Token> optionalToken =
                this.tokenRepository.findByValueAndIsDeletedFalseAndExpiryAtAfter(
                        tokenValue, LocalDateTime.now());
        if (optionalToken.isEmpty()) {
            throw new InvalidTokenException("Token is invalid");
        }

        Token token = optionalToken.get();

        return token.getUser();
    }
}
