package com.project.ecommerce.user_service.repositories;

import com.project.ecommerce.user_service.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Token save(Token token);

    List<Token> findAllByUser_IdAndIsDeletedFalse(Long userId);

    Optional<Token> findByValueAndIsDeletedFalse(String value);

    Optional<Token> findByValueAndIsDeletedFalseAndExpiryAtAfter(String value, LocalDateTime now);
}
