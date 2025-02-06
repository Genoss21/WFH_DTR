package dev.tgsi.attendance_registration_system.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.repository.UserRepository;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    // Constructor injection for better testability and immutability
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Use Optional for better handling of null values
        Optional<User> userOptional = userRepository.findByUsername(username);

        // Throw exception if user is not found
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Return the custom user details object
        return new CustomUserDetail(user);
    }
}
