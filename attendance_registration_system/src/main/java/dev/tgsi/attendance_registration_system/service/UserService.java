package dev.tgsi.attendance_registration_system.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import dev.tgsi.attendance_registration_system.repository.UserRepository;
import dev.tgsi.attendance_registration_system.models.User;


@Service
public class UserService {
    private final UserDetailsService userDetailsService;

     public UserService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public UserDetails getUserDetails(String username) {
        return userDetailsService.loadUserByUsername(username);
    }


    @Autowired
    private UserRepository userRepository;

    public String getUserImagePath(String empId) {
        Optional<User> userOptional = userRepository.findById(empId);
        return userOptional.map(user -> (String) user.getImgSrc()).orElse(null);
    }
    


    
}

