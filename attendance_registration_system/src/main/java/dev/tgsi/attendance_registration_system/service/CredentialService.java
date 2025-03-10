package dev.tgsi.attendance_registration_system.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.repository.UserRepository;

@Service
public class CredentialService {
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Return a list of credentials
     * for all users in the system.
     * @return a list of UserCredentials objects
     */
    public List<UserCredentials> getAllCredentials() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserCredentials(user.getUsername()))
                .collect(Collectors.toList());
    }

    public static class UserCredentials {
        private String username;

        public UserCredentials(String username) {
            this.username = username;
        }

        public String getUsername() {
            return username;
        }

       
    }
    
}
