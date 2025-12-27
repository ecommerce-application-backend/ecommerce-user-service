package com.project.ecommerce.user_service.dtos;

import com.project.ecommerce.user_service.enums.ResponseStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private String token;
    private ResponseStatus responseStatus;
}
