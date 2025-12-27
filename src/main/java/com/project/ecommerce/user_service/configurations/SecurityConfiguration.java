//package com.project.ecommerce.user_service.configurations;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;

/**
 * This class was created initially before the Spring Authorization Server was incorporated in our application.
 * The purpose of this class was to bypass all endpoints from Spring Security.
 */

//@Configuration
//public class SecurityConfiguration {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((requests) -> {
//                    try {
//                        requests
//                                .anyRequest().permitAll()
//                                .and().cors().disable()
//                                .csrf().disable();
//                    } catch (Exception exception) {
//                        throw new RuntimeException(exception);
//                    }
//                });
//        return http.build();
//    }
//
//}
