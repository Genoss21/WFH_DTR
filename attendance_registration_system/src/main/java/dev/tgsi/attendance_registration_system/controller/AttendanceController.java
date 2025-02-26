// !Added 02/11/2025

package dev.tgsi.attendance_registration_system.controller;

import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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

import org.springframework.ui.Model;

import dev.tgsi.attendance_registration_system.repository.AttendanceRepository;
import dev.tgsi.attendance_registration_system.repository.UserRepository;
import dev.tgsi.attendance_registration_system.service.ActivityLogService;
import dev.tgsi.attendance_registration_system.service.AttendanceService;
import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import dev.tgsi.attendance_registration_system.models.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

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

    // ! For Pagination
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
}
// !End of file
