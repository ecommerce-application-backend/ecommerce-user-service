package com.project.ecommerce.user_service.dtos;

import com.project.ecommerce.user_service.enums.ResponseStatus;
import com.project.ecommerce.user_service.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDto {
    private String email;
    private String name;
    private List<String> roles;
    private ResponseStatus status;

    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        List<String> roles = new ArrayList<>();
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            user.getRoles().forEach(role -> roles.add(role.getValue()));
        }
        userDto.setRoles(roles);
        return userDto;
    }
}
