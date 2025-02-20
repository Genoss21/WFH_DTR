package dev.tgsi.attendance_registration_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.Authentication;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.repository.UserRepository;

import java.util.List;

import dev.tgsi.attendance_registration_system.dto.FilterDates;
import dev.tgsi.attendance_registration_system.dto.LeaveDto;
import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import dev.tgsi.attendance_registration_system.models.PersonalInfoModel;
import dev.tgsi.attendance_registration_system.service.AttendanceService;
import dev.tgsi.attendance_registration_system.service.ProjectService;
import dev.tgsi.attendance_registration_system.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    
    @Autowired
    private  AttendanceService attendanceService;

    @Autowired
    private ProjectService projectService;


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/login")
    public String login() {
        return "Login1";
    }

    @GetMapping("/user-page")
    public String userPage(Model model, Principal principal,@ModelAttribute FilterDates filterDates) {
        if (principal == null) {
            logger.error("No authenticated user found.");
            model.addAttribute("error", "No authenticated user found");
            return "Login1";
        }

        String username = principal.getName();
        logger.info("Authenticated username: " + username);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            logger.info("User details: " + user.toString()); // Debug log
            model.addAttribute("user", user);
        } else {
            logger.error("User not found: " + username);
            model.addAttribute("error", "User not found");
        }

        // Add the employees list to the model
        List<PersonalInfoModel> employees = userService.getAllEmployees();
        model.addAttribute("employees", employees);

        // !Added
        // !Author: Stvn
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        //String empId = user.getEmpId();
        //model.addAttribute("records", attendanceService.getUserAttendance(user));
        model.addAttribute("isClockedIn", attendanceService.isUserClockedIn(user));
        model.addAttribute("latestTimeIn", attendanceService.getLatestTimeIn(user));
        model.addAttribute("latestTimeOut", attendanceService.getLatestTimeOut(user));
        model.addAttribute("date",filterDates);

        List<AttendanceRecord> attendanceRecords;

        if(filterDates.getStartDate()!=null && !filterDates.getStartDate().isEmpty() 
            && filterDates.getEndDate()!= null && !filterDates.getEndDate().isEmpty()){

           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
           LocalDate startdDate =  LocalDate.parse(filterDates.getStartDate(),formatter);
           LocalDate endDate =  LocalDate.parse(filterDates.getEndDate(),formatter);
           attendanceRecords = attendanceService.getAttendanceRecordByDate(user,startdDate,endDate);
           if (attendanceRecords != null && !attendanceRecords.isEmpty()) {
            model.addAttribute("records", attendanceRecords);
            } else {
            //model.addAttribute("records", new ArrayList<>());
            model.addAttribute("filterError", "No attendance records found for the specified date range.");
            }
        }
        else{
            model.addAttribute("records", attendanceService.getUserAttendance(user));
        }

        LeaveDto leaveDto = new LeaveDto();
        model.addAttribute("leaveDto", leaveDto);   
        // !end of added

        return "Emp_dashboard";
    }


    @GetMapping("/admin-page")
    public String adminPage(Model model, Principal principal, 
                        @ModelAttribute FilterDates filterDates, 
                        @RequestParam(value="memberId" ,required = false) String memberId,
                        @RequestParam(value="projectId" ,required = false) String projectId) {

        if (principal == null) {
            logger.error("No authenticated user found.");
            model.addAttribute("error", "No authenticated user found");
            return "Login1";
        }

        String username = principal.getName();
        logger.info("Authenticated username: " + username);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            logger.info("User details: " + user.toString()); // Debug log
            model.addAttribute("user", user);
        } else {
            logger.error("User not found: " + username);
            model.addAttribute("error", "User not found");
        }

        // Add the employees list to the model
        List<PersonalInfoModel> employees = userService.getAllEmployees();
        model.addAttribute("employees", employees);

        // !Added
        // !Author: Stvn 
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        //String empId = user.getEmpId();
        model.addAttribute("isClockedIn", attendanceService.isUserClockedIn(user));
        model.addAttribute("latestTimeIn", attendanceService.getLatestTimeIn(user));
        model.addAttribute("latestTimeOut", attendanceService.getLatestTimeOut(user));
        model.addAttribute("date",filterDates);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate startdDate = null;
        LocalDate endDate = null;

        if (filterDates.getStartDate() != null && !filterDates.getStartDate().isEmpty() 
            && filterDates.getEndDate() != null && !filterDates.getEndDate().isEmpty()) {
            startdDate = LocalDate.parse(filterDates.getStartDate(), formatter);
            endDate = LocalDate.parse(filterDates.getEndDate(), formatter);
        }

        List<AttendanceRecord> attendanceRecords;
        if( (memberId == null || memberId.isEmpty()) && startdDate != null && endDate != null ){

           attendanceRecords = attendanceService.getAttendanceRecordByDate(user,startdDate,endDate);

           if (attendanceRecords != null && !attendanceRecords.isEmpty()) {
            model.addAttribute("records", attendanceRecords);
            } else {
            //model.addAttribute("records", new ArrayList<>());
            model.addAttribute("filterError", "No attendance records found for the specified date range.");
            }

        }else if( memberId != null && startdDate != null && endDate != null ){

            attendanceRecords = attendanceService.getAttendanceRecordByMembersAndDate(memberId,startdDate,endDate);
            if (attendanceRecords != null && !attendanceRecords.isEmpty()) {
                model.addAttribute("records", attendanceRecords);
            } else {
                model.addAttribute("filterError", "No attendance records found for the specified date range.");
            }
        }
        else{
            model.addAttribute("records", attendanceService.getUserAttendance(user));
        }
        // !end of added

        model.addAttribute("selectedMemberId", memberId);
        model.addAttribute("selectedProjectId", projectId);

        LeaveDto leaveDto = new LeaveDto();
        model.addAttribute("leaveDto", leaveDto);

        model.addAttribute("projects", projectService.projectList(user));

        return "Mngr_dashboard";
    }


    @GetMapping(value = "/user-image/{empId}")
    @ResponseBody
    public ResponseEntity<Resource> getUserImage(@PathVariable("empId") String empId) {
        logger.info("Fetching image for empId: " + empId);
        String imagePath = userService.getUserImagePath(empId);
        if (imagePath == null) {
            logger.error("Image not found for empId: " + empId);
            return ResponseEntity.notFound().build();
        }
        Path path = Paths.get(imagePath);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            logger.error("Malformed URL for path: " + imagePath, e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}
