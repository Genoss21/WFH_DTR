// !Added 02/11/2025

package dev.tgsi.attendance_registration_system.controller;

import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import org.springframework.ui.Model;

import dev.tgsi.attendance_registration_system.repository.UserRepository;
import dev.tgsi.attendance_registration_system.service.AttendanceService;
import dev.tgsi.attendance_registration_system.models.User;


@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        //String empId = user.getEmpId();
        model.addAttribute("records", attendanceService.getUserAttendance(user));
        model.addAttribute("isClockedIn", attendanceService.isUserClockedIn(user));
        model.addAttribute("username", username);
        
        return "Emp_dashboard";
    }

    @PostMapping("/clock-in")
    @ResponseBody
    public String clockIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        attendanceService.saveTimeIn(user);
        Map<String, String> response = new HashMap<>();
        response.put("title", "Good Morning");
        response.put("message", "Time-in Successfully");
        response.put("status", "success");
        return new Gson().toJson(response);
    }

    @PostMapping("/clock-out")
    @ResponseBody
    public String clockOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        attendanceService.saveTimeOut( user);
        Map<String, String> response = new HashMap<>();
        response.put("title", "Good Bye!");
        response.put("message", "Time-out Successfully");
        response.put("status", "success");
        return new Gson().toJson(response);
    }

    @GetMapping("/check-status")
    @ResponseBody
    public boolean checkStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        //String empId = user.getEmpId();
        return attendanceService.isUserClockedIn(user);

    }

    @GetMapping("/user/delete/{attendanceId}")
    public String deleteUserAttendance(@PathVariable(name="attendanceId") Long id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        attendanceService.deleteAttendance(id,user);
        return "redirect:/user-page";
    }

    @GetMapping("/admin/delete/{attendanceId}")
    public String deleteAdminAttendance(@PathVariable(name="attendanceId") Long id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        attendanceService.deleteAttendance(id,user);
        return "redirect:/admin-page";
    }
}
// !End of file
