package com.project.ecommerce_user_service.security.models;

import com.project.ecommerce_user_service.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class is defined because we don't want to use default user defined in /security/SecurityConfig
 * class. But we want to take Users from DB
 *
 * This CustomUserDetails class will act like a User class for Spring Security.
 */
public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private List<CustomGrantedAuthority> grantedAuthorities;

    public CustomUserDetails(User user) {
        this.username = user.getEmail();
        this.password = user.getHashedPassword();
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = user.isEmailVerified();

        // We need to map user Role with Granted Authority
        this.grantedAuthorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            grantedAuthorities.add(new CustomGrantedAuthority(role));
        });
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
