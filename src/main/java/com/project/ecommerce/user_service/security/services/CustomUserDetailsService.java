package com.project.ecommerce.user_service.security.services;

import com.project.ecommerce.user_service.models.User;
import com.project.ecommerce.user_service.repositories.UserRepository;
import com.project.ecommerce.user_service.security.models.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // As we've mapped email in our User class to username in UserDetails class provided
        // by Spring Security
        Optional<User> optionalUser = this.userRepository.findByEmail(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with username: " + username + " doesn't exist");
        }

        return new CustomUserDetails(optionalUser.get());
    }
}
