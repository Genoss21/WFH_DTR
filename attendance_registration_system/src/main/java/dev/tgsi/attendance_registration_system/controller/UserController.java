package dev.tgsi.attendance_registration_system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Optional;

import dev.tgsi.attendance_registration_system.repository.UserRepository;
import dev.tgsi.attendance_registration_system.models.User;
@Controller
public class UserController {



    @GetMapping("/login")
    public String login() {
        return "login";
    }

     @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user-page")
    public String employeeDashboard(Model model, Principal principal) {
        if (principal == null) {
            logger.error("No authenticated user found.");
            model.addAttribute("error", "No authenticated user found");
            return "login";
        }

        String username = principal.getName();
        logger.info("Authenticated username: " + username);

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
        } else {
            logger.error("User not found: " + username);
            model.addAttribute("error", "User not found");
        }

        return "employee_dashboard";
    }


    @GetMapping("/admin-page")
    public String adminPage(Model model, Principal principal) {
        if (principal == null) {
            logger.error("No authenticated user found.");
            model.addAttribute("error", "No authenticated user found");
            return "login";
        }

        String username = principal.getName();
        logger.info("Authenticated username: " + username);

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
        } else {
            logger.error("User not found: " + username);
            model.addAttribute("error", "User not found");
        }
        
        return "manager_dashboard";
    }


	
}
