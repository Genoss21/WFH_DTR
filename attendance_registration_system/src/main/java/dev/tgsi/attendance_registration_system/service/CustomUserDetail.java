package dev.tgsi.attendance_registration_system.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import dev.tgsi.attendance_registration_system.models.User;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetail implements UserDetails {
    
    private User user;

    // Constructor
    public CustomUserDetail(User user) {
        if (user == null || user.getRole() == null) {
            throw new IllegalArgumentException("User and User role cannot be null");
        }
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Using Simple Granted Authority to return the role as an authority
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getRoleShName()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Assuming account never expires, could be modified based on business logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Modify as per your logic for account lock status
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Modify as per your logic for credential expiry
    }

    @Override
    public boolean isEnabled() {
        // Modify based on your business logic if a user is enabled/disabled
        return true; // Assuming always enabled, change based on status in the DB
    }

    // Getters for User object if needed
    public User getUser() {
        return user;
    }
}
