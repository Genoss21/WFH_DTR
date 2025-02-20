package dev.tgsi.attendance_registration_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import dev.tgsi.attendance_registration_system.service.TimeLogService;

@Controller
public class DashboardController {

    private final TimeLogService timeLogService;

    public DashboardController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Replace with your actual logic to retrieve the record.
        // For example, get the current user's record.
        AttendanceRecord attendanceRecord = timeLogService.findRecordForEditing();

        // Option 1: If null, you might want to create a new instance or handle it accordingly.
        if (attendanceRecord == null) {
            attendanceRecord = new AttendanceRecord();
        }
        
        model.addAttribute("attendanceRecord", attendanceRecord);
        return "Emp_dashboard";
    }
}
