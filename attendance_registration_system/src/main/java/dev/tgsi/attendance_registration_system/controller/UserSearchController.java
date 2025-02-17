package dev.tgsi.attendance_registration_system.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.tgsi.attendance_registration_system.dto.UserSearchDto;
import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import dev.tgsi.attendance_registration_system.models.PersonalInfoModel;
import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.repository.AttendanceRepository;
import dev.tgsi.attendance_registration_system.repository.PersonalInfoRepository;

@RestController
@RequestMapping("/api/users")
public class UserSearchController {

    @Autowired
    private PersonalInfoRepository personalInfoRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @GetMapping("/search")
    public ResponseEntity<List<UserSearchDto>> searchUsers(@RequestParam(required = false) String query) {
        List<PersonalInfoModel> employees;
        
        if (query == null || query.trim().isEmpty()) {
            // Return all employees if no query is provided
            employees = personalInfoRepository.findAll();
        } else {
            // Search by firstName, lastName, or email
            employees = personalInfoRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                query, query, query
            );
        }

        LocalDate today = LocalDate.now();
        List<UserSearchDto> results = employees.stream().map(employee -> {
            User user = employee.getUser();
            AttendanceRecord attendanceRecord = attendanceRepository.findTodayAttendance(user.getEmpId(), today);
            String status = (attendanceRecord != null) ? attendanceRecord.getStatus().name() : "OFFLINE";

            String fullName = employee.getFirstName() + " " + employee.getLastName();
            return new UserSearchDto(fullName, employee.getEmail(), user.getImgSrc(), status);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }
}
