package com.project.ecommerce.user_service.controllers;

import com.project.ecommerce.user_service.dtos.*;
import com.project.ecommerce.user_service.enums.ResponseStatus;
import com.project.ecommerce.user_service.models.Token;
import com.project.ecommerce.user_service.models.User;
import com.project.ecommerce.user_service.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/signup")
    public UserDto signUp(@RequestBody SignUpRequestDto requestDto) {
        UserDto responseDto = new UserDto();
        try {
            User user = this.userService.signUp(
                    requestDto.getName(), requestDto.getEmail(), requestDto.getPassword());
            responseDto = UserDto.from(user);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch(Exception exception) {
            responseDto.setStatus(ResponseStatus.ERROR);
        }

        return responseDto;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto requestDto) {
        LoginResponseDto responseDto = new LoginResponseDto();
        try {
            Token token = this.userService.login(requestDto.getEmail(), requestDto.getPassword());
            responseDto.setToken(token.getValue());
            responseDto.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception exception) {
            responseDto.setResponseStatus(ResponseStatus.ERROR);
        }
        return responseDto;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto requestDto) {
        try {
            this.userService.logout(requestDto.getToken());
        } catch (Exception exception) {
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/validate/{token}")
    public UserDto validateToken(@PathVariable String token) {
        UserDto userDto = new UserDto();
        try {
            User user = this.userService.validateToken(token);
            userDto = UserDto.from(user);
            userDto.setStatus(ResponseStatus.SUCCESS);
        } catch (Exception exception) {
            userDto.setStatus(ResponseStatus.ERROR);
        }
        return userDto;
    }
}
