// !Added 02/11/2025

package dev.tgsi.attendance_registration_system.controller;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

import dev.tgsi.attendance_registration_system.repository.AttendanceRepository;
import dev.tgsi.attendance_registration_system.repository.UserRepository;
import dev.tgsi.attendance_registration_system.service.AttendanceService;
import dev.tgsi.attendance_registration_system.models.User;


@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String empId = user.getEmpId();
        model.addAttribute("records", attendanceService.getUserAttendance(empId));
        model.addAttribute("isClockedIn", attendanceService.isUserClockedIn(empId));
        model.addAttribute("username", username);
        

        return "employee_dashboard";
    }

    @PostMapping("/clock-in")
    @ResponseBody
    public String clockIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String empId = user.getEmpId();
        attendanceService.clockIn(empId);
        return "Clocked in successfully";
    }

    @PostMapping("/clock-out")
    @ResponseBody
    public String clockOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String empId = user.getEmpId();
        attendanceService.clockOut(empId);
        return "Clocked out successfully";
    }

    @GetMapping("/check-status")
    @ResponseBody
    public boolean checkStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String empId = user.getEmpId();
        return attendanceService.isUserClockedIn(empId);

    }
}
// !End of file
