package dev.tgsi.attendance_registration_system.controller;

import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import dev.tgsi.attendance_registration_system.repository.AttendanceRepository;
import dev.tgsi.attendance_registration_system.repository.UserRepository;
import dev.tgsi.attendance_registration_system.service.ActivityLogService;
import dev.tgsi.attendance_registration_system.service.AttendanceService;
import dev.tgsi.attendance_registration_system.service.ExcelExportService;
import dev.tgsi.attendance_registration_system.dto.AttendanceDto;
import dev.tgsi.attendance_registration_system.dto.TargetDateTime;
import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import dev.tgsi.attendance_registration_system.models.User;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ExcelExportService excelExportService;

    @PostMapping("/clock-in")
    @ResponseBody
    public String clockIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, String> response = attendanceService.saveTimeIn(user);
        
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

    // For Delete
    @GetMapping("/delete/{attendanceId}")
    public String deleteUserAttendance(@PathVariable(name="attendanceId") Long id){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        attendanceService.deleteAttendance(id,user);
        // !Added JDC 02/27/2025
        activityLogService.saveLog("Deleted attendance record" , user);
        return "Attendance record successfully deleted!";
    }

    // For Edit And Delete
    @GetMapping("/record/{attendanceId}")
    @ResponseBody
    public AttendanceRecord getAttendanceById(@PathVariable long attendanceId) {
        return attendanceRepository.findByAttendanceId(attendanceId);
        }

    @RequestMapping(value = "/edit", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> updateAttendance(@RequestBody Map<String, String> payload){

        Long id = Long.parseLong(payload.get("id"));
        String timeIn = payload.get("timeIn");
        String timeOut = payload.get("timeOut");
        String remarks = payload.get("remarks");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        attendanceService.updateAttendance(id,user,timeIn,timeOut,remarks);
        activityLogService.saveLog("Edited attendance record" , user);
        return ResponseEntity.ok("Successfully edited attendance record");
    }

    // * For Pagination
    @GetMapping("/paginated")
    @ResponseBody
    public Page<AttendanceRecord> getPaginatedAttendance(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "7") int size,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            return attendanceService.getUserAttendancePaginatedByDate(user, start, end, page, size);
        }

        return attendanceService.getUserAttendancePaginated(user, page, size);
    }

    @GetMapping("/export-excel")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(required = false) String empId
        ) {

        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // If empId is provided, use it (for admin/manager view), otherwise use the current user's empId
            String targetEmpId = empId != null ? empId : user.getEmpId();
            User targetUser = userRepository.findByEmpId(targetEmpId)
                    .orElseThrow(() -> new RuntimeException("Target user not found"));

            // Get attendance records
            List<AttendanceRecord> records;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            
            if (startDate != null && endDate != null) {
                LocalDate start = LocalDate.parse(startDate,formatter);
                LocalDate end = LocalDate.parse(endDate,formatter);
                records = attendanceRepository.getAttendanceRecordByDate(targetUser.getEmpId(), start, end);
            } else {
                records = attendanceRepository.getAttendanceRecordByMember(targetUser.getEmpId());
            }

            // Generate Excel file
            byte[] excelContent = excelExportService.exportToExcel(
                records, 
                targetUser.getUsername(),
                targetUser.getEmpId(),
                targetUser.getPersonalInfo() != null ? targetUser.getPersonalInfo().getEmail() : ""
            );
            // Format current timestamp for filename
            TargetDateTime dateTime = new TargetDateTime();
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH_mm");
            String date = dateTime.getDateNow().toString();
            String time = dateTime.getTimeNow().format(timeFormatter).toString();
            String  filename = "Attendance_Report_" + date +"_"+ time+ ".xlsx";
            

            // Set up the response
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);

            return new ResponseEntity<>(excelContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/preview")
    public ResponseEntity<List<AttendanceDto>> getAttendancePreview(@RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String empId
    ) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

               
        String targetEmpId = empId != null ? empId : user.getEmpId();
        User targetUser = userRepository.findByEmpId(targetEmpId)
                        .orElseThrow(() -> new RuntimeException("User not found"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        

        List<AttendanceRecord> records;
        if (startDate != null && endDate != null) {
            LocalDate start = LocalDate.parse(startDate, formatter);
            LocalDate end = LocalDate.parse(endDate, formatter);
            records = attendanceRepository.getAttendanceRecordByDate(targetUser.getEmpId(),start,end);
        }
        else{
            records = attendanceRepository.getAttendanceRecordByMember(targetUser.getEmpId());
        }

        List<AttendanceDto> attendancePreview = records.stream().map(attendance -> {

        String fullName = attendance.getUser().getPersonalInfo().getFirstName() + " " + attendance.getUser().getPersonalInfo().getLastName();
        return new AttendanceDto(attendance.getUser().getEmpId(),fullName,targetUser.getPersonalInfo().getEmail(),attendance.getDate(),attendance.getTimeIn(),
                attendance.getTimeOut(),attendance.getEditedByName(),attendance.getRemarks());
        }).collect(Collectors.toList());

        return ResponseEntity.ok(attendancePreview);
    }
}
// !End of file
