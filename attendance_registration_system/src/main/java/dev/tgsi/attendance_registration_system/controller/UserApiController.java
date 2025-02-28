package dev.tgsi.attendance_registration_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.repository.UserRepository;


@RestController
public class UserApiController {

    @Autowired
    UserRepository userRepository;

    // * For Register New User using postman
    // * For testing only
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        String encoded =  new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encoded);
        userRepository.save(user);
        return user;
    }
    
}