package dev.tgsi.attendance_registration_system.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import dev.tgsi.attendance_registration_system.service.TimeLogService;

@RestController
@RequestMapping("/api/time-log")
public class TimeLogController {

    private final TimeLogService timeLogService;

    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    @PostMapping("/edit")
    public ResponseEntity<AttendanceRecord> editTimeLog(
            @RequestParam Long attendanceId,
            @RequestParam("time_in") String timeInStr,
            @RequestParam("time_out") String timeOutStr,
            @RequestParam String remarks) {

        // Define formatter matching the expected input format (e.g., "06:01:01 AM")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");

        // Parse the time strings into LocalTime objects
        LocalTime newTimeIn = LocalTime.parse(timeInStr, formatter);
        LocalTime newTimeOut = LocalTime.parse(timeOutStr, formatter);

        // Delegate the update operation to the service
        AttendanceRecord updatedRecord = timeLogService.updateTimeLog(attendanceId, newTimeIn, newTimeOut, remarks);

        return ResponseEntity.ok(updatedRecord);
    }
}
