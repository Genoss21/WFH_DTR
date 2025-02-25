// !Added 02/11/2025
// !Author: Stvn
package dev.tgsi.attendance_registration_system.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.repository.AttendanceRepository;
import dev.tgsi.attendance_registration_system.repository.UserRepository;




@RestController
@RequestMapping("/api")
public class AttendanceApiController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;


    @GetMapping("/check-clocked-in")
    public ResponseEntity<Map<String, Object>> checkClockedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Optional<User> userOptional = userRepository.findByUsername(currentUsername);

        if (userOptional.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", true);
            errorResponse.put("message", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        User user = userOptional.get();
        //boolean clockIn = !attendanceRepository.findByUser_EmpIdAndTimeOutIsNull(user.getEmpId()).isEmpty();
        AttendanceRecord attendanceRecord = attendanceRepository.findTodayAttendance(user.getEmpId());
        boolean clockIn = attendanceRecord == null ? false : true;

        Map<String, Object> response = new HashMap<>();
        response.put("clockedIn", clockIn);
        return ResponseEntity.ok(response);

    }
}
// !End of file