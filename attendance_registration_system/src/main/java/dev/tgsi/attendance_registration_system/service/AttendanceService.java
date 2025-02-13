// ! Added 02/11/2025

package dev.tgsi.attendance_registration_system.service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dev.tgsi.attendance_registration_system.models.User;

import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import dev.tgsi.attendance_registration_system.models.LeaveModel;
import dev.tgsi.attendance_registration_system.repository.AttendanceRepository;
import dev.tgsi.attendance_registration_system.repository.LeaveRepository;
import dev.tgsi.attendance_registration_system.repository.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class AttendanceService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    public void clockIn(String empId) {
        User user = userRepository.findByEmpId(empId)
            .orElseThrow(() -> new RuntimeException("User not found with emp ID: " + empId));

        if (isUserClockedIn(empId)) {
            throw new RuntimeException("User is already clocked in");   
        }

        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setUser(user);
        attendanceRecord.setTimeIn(LocalDateTime.now());
        attendanceRepository.save(attendanceRecord);
    }

    public void clockOut(String empId) {
        List<AttendanceRecord> openRecords = attendanceRepository.findByUser_EmpIdAndTimeOutIsNull(empId);

        if (openRecords.isEmpty()) {
            throw new RuntimeException("User is not clocked in");
        }

        AttendanceRecord record = openRecords.get(0);
        record.setTimeOut(LocalDateTime.now());
        attendanceRepository.save(record);
    }


    public List<AttendanceRecord> getUserAttendance(String empId) {
        // Extensive debugging
        System.out.println("DEBUG: Attempting to fetch attendance records for empId: " + empId);
        
        // Validate input
        if (empId == null || empId.trim().isEmpty()) {
            System.err.println("DEBUG: Invalid empId provided");
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }

        // Check if user exists first
        User user = userRepository.findByEmpId(empId)
            .orElseThrow(() -> {
                System.err.println("DEBUG: User not found with empId: " + empId);
                return new RuntimeException("User not found with emp ID: " + empId);
            });
        
        System.out.println("DEBUG: User found - Username: " + user.getUsername());
        System.out.println("DEBUG: User Details - EmpId: " + user.getEmpId());
        
        // Fetch records with detailed logging
        List<AttendanceRecord> records = attendanceRepository.findByUser_EmpId(empId);
        
        System.out.println("DEBUG: Total records fetched: " + records.size());
        
        // Log details of each record
        for (AttendanceRecord record : records) {
            System.out.println("DEBUG: Record Details:");
            System.out.println("  - Attendance ID: " + record.getAttendanceId());
            System.out.println("  - Time In: " + record.getTimeIn());
            System.out.println("  - Time Out: " + record.getTimeOut());
            
            // Additional debug: check the associated user
            if (record.getUser() != null) {
                System.out.println("  - Associated User EmpId: " + record.getUser().getEmpId());
            } else {
                System.err.println("  - WARNING: Record has no associated user!");
            }
        }
        
        return records;
    }


    public boolean isUserClockedIn(String empId) {
        List<AttendanceRecord> openRecords = attendanceRepository.findByUser_EmpIdAndTimeOutIsNull(empId);
        return !openRecords.isEmpty();
    }
    

    public LocalDateTime getLatestTimeIn(String empId) {
        List<AttendanceRecord> userRecords = attendanceRepository.findByUser_EmpId(empId);

        return userRecords.stream()
            .filter(record -> record.getTimeOut() != null)
            .map(AttendanceRecord::getTimeIn)
            .max(LocalDateTime::compareTo)
            .orElse(null);
    }


    public LocalDateTime getLatestTimeOut(String empId) {
        List<AttendanceRecord> userRecords = attendanceRepository.findByUser_EmpIdAndTimeOutIsNull(empId);

        if (!userRecords.isEmpty()) {
            return null;
        }

            return attendanceRepository.findByUser_EmpId(empId)
                .stream()
                .filter(record -> record.getTimeOut() != null)
                .map(AttendanceRecord::getTimeOut)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        }

    @Transactional
    public AttendanceRecord saveLeave(LeaveModel leaveModel , User user) {
        AttendanceRecord attendanceModel =  new AttendanceRecord();
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        leaveRepository.save(leaveModel);
        attendanceModel.setLeaveModel(leaveModel);
        attendanceModel.setUser(user);
        attendanceModel.setDate(leaveModel.getLeaveDate());
        attendanceModel.setCreatedOn(LocalDateTime.parse(now.toString()));
        attendanceModel.setUpdatedOn(LocalDateTime.parse(now.toString()));
        attendanceModel.setDelFlag(0);
        attendanceModel.setStatus("on Leave");
        attendanceModel.setEditedById(user.getEmpId());
        attendanceModel.setEditedByRole(user.getRole().getRoleShName());
        attendanceModel.setRemarks("on Leave");
        return attendanceRepository.save(attendanceModel);

    }
    
}
// ! End