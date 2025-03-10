package dev.tgsi.attendance_registration_system.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.tgsi.attendance_registration_system.dto.TargetDateTime;
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

        
        TargetDateTime dateTime = new TargetDateTime();
        List<UserSearchDto> results = employees.stream().map(employee -> {
            User user = employee.getUser();
            // Retrieve today’s attendance record, if it exists
            AttendanceRecord attendanceRecord = attendanceRepository.findByDate(user.getEmpId(), dateTime.getDateNow());
            String status = "Offline"; // default status

            if (attendanceRecord != null) {
                
                switch (attendanceRecord.getStatus()) {
                    case ONLINE:
                        status = "Online";
                        break;
                    case ON_LEAVE:
                        status = "On leave";
                    default:
                        break;
                    
                }

                if(attendanceRecord.getStatus() == AttendanceRecord.Status.ON_LEAVE){
                    attendanceRecord = attendanceRepository.findByDate(user.getEmpId(),dateTime.getPreviousDay());

                    if(attendanceRecord !=null && attendanceRecord.getTimeOut() == null ){
                        status = "Online";
                    }
                }
            }
            else{
                 attendanceRecord = attendanceRepository.findByDate(user.getEmpId(),dateTime.getPreviousDay());

                 if(attendanceRecord != null && attendanceRecord.getTimeOut() == null){ 
                    status = "Online";
                 }

            }

            String fullName = employee.getFirstName() + " " + employee.getLastName();
            return new UserSearchDto(fullName, employee.getEmail(),user.getEmpId(), user.getImgSrc(), status);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(results);
    }

}
