package com.project.ecommerce.user_service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Token extends BaseModel{
    private String value;
    @ManyToOne
    private User user;
    private LocalDateTime expiryAt;
}
