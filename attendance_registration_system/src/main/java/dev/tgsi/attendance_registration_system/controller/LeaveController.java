// package dev.tgsi.attendance_registration_system.controller;

// import java.security.Principal;
// import java.time.LocalDate;
// import java.time.format.DateTimeFormatter;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.PostMapping;

// import dev.tgsi.attendance_registration_system.dto.LeaveDto;
// import dev.tgsi.attendance_registration_system.models.LeaveModel;
// import dev.tgsi.attendance_registration_system.models.User;
// import dev.tgsi.attendance_registration_system.repository.UserRepository;
// import dev.tgsi.attendance_registration_system.service.ActivityLogService;
// import dev.tgsi.attendance_registration_system.service.AttendanceService;

// @Controller
// public class LeaveController {

//     @Autowired
//     private AttendanceService attendanceService;

//     @Autowired
//     private UserRepository userRepository;

//     @Autowired
//     private ActivityLogService activityLogService;

//     @PostMapping("/user-page/file-leave")
//     public String saveLeaveEmp(LeaveModel leaveModel, Principal principal, LeaveDto leaveDto) {
        
//         User user =  userRepository.findByUsername(principal.getName())
//         .orElseThrow(() -> new RuntimeException("User not found"));
     
//         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//         LocalDate leaveDate = LocalDate.parse(leaveDto.getLeaveDateDto(), formatter);
//         leaveModel.setLeaveDate(leaveDate);
//         leaveModel.setLeaveType(leaveDto.getLeaveTypeDto());
//         leaveModel.setLeaveDuration(leaveDto.getLeaveDurationDto());
//         leaveModel.setRemarks(leaveDto.getRemarksDto());
//         attendanceService.saveLeave(leaveModel, user);
//         activityLogService.saveLog("Applied leave for " + leaveModel.getLeaveDate(), user);
//         return "redirect:/user-page";
//     }

//     @PostMapping("/admin-page/file-leave")
//     public String saveLeaveMngr(LeaveModel leaveModel, Principal principal, LeaveDto leaveDto) {
        
//         User user =  userRepository.findByUsername(principal.getName())
//         .orElseThrow(() -> new RuntimeException("User not found"));
     
//         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//         LocalDate leaveDate = LocalDate.parse(leaveDto.getLeaveDateDto(), formatter);
//         leaveModel.setLeaveDate(leaveDate);
//         leaveModel.setLeaveType(leaveDto.getLeaveTypeDto());
//         leaveModel.setLeaveDuration(leaveDto.getLeaveDurationDto());
//         leaveModel.setRemarks(leaveDto.getRemarksDto());
//         attendanceService.saveLeave(leaveModel, user);
//         activityLogService.saveLog("Applied leave for " + leaveModel.getLeaveDate(), user);
//         return "redirect:/admin-page";
//     }
    
// }
