package dev.tgsi.attendance_registration_system.controller;

import java.security.Principal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    private final UserDetailsService userDetailsService;

    public UserController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user-page")
    public String userPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirect if user is not authenticated
        }
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "employee_dashboard";
    }

    @GetMapping("/admin-page")
    public String adminPage(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login"; // Redirect if user is not authenticated
        }
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "manager_dashboard";
    }


	
}
