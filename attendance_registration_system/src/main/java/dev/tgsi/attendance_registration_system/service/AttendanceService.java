// ! Added 02/11/2025

package dev.tgsi.attendance_registration_system.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.models.AttendanceRecord.Status;
import dev.tgsi.attendance_registration_system.dto.AttendanceDto;
import dev.tgsi.attendance_registration_system.dto.TargetDateTime;
import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import dev.tgsi.attendance_registration_system.models.LeaveModel;
import dev.tgsi.attendance_registration_system.repository.AttendanceRepository;
import dev.tgsi.attendance_registration_system.repository.LeaveRepository;
//import dev.tgsi.attendance_registration_system.repository.UserRepository;
import jakarta.transaction.Transactional;

// Pagination
import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class AttendanceService {

   // @Autowired
    //private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    /*public void clockIn(String empId) {
        User user = userRepository.findByEmpId(empId)
            .orElseThrow(() -> new RuntimeException("User not found with emp ID: " + empId));

        if (isUserClockedIn(empId)) {
            throw new RuntimeException("User is already clocked in");   
        }

        AttendanceRecord attendanceRecord = new AttendanceRecord();
        attendanceRecord.setUser(user);
        attendanceRecord.setDate(LocalDate.now()); // set today's date
        attendanceRecord.setTimeIn(LocalTime.now()); // time-in record
        attendanceRecord.setStatus(Status.ONLINE); // set status
        attendanceRecord.setCreatedOn(LocalDateTime.now()); // set created on
        attendanceRecord.setUpdatedOn(LocalDateTime.now()); // set updated on
        attendanceRepository.save(attendanceRecord);
    }
    */

    public void saveTimeIn(User user) {
        TargetDateTime dateTime = new TargetDateTime();
        AttendanceRecord attendanceRecord = attendanceRepository.findByDate(user.getEmpId(),dateTime.getTargetDate());

        LocalDateTime now = LocalDateTime.now();
        LocalTime timeIn = LocalTime.now();

        if(attendanceRecord != null)
        {
            if(attendanceRecord.getStatus() == Status.ONLINE)
            {
                System.out.println("Already Login");

            }else if(attendanceRecord.getStatus() == Status.ON_LEAVE)
            {
                attendanceRecord.setStatus(Status.ONLINE);
                attendanceRecord.setTimeIn(LocalTime.parse(timeIn.toString()));
                attendanceRecord.setUpdatedOn(LocalDateTime.parse(now.toString()));
                attendanceRepository.save(attendanceRecord);
            }
            else
            {
                System.out.println("User is offline");
            }
        }
        else
        {
        AttendanceRecord attendanceModel = new AttendanceRecord();
        attendanceModel.setUser(user);
        attendanceModel.setDate(LocalDate.parse(dateTime.getTargetDate().toString()));
        attendanceModel.setTimeIn(LocalTime.parse(timeIn.toString()));
        attendanceModel.setCreatedOn(LocalDateTime.parse(now.toString()));
        attendanceModel.setUpdatedOn(LocalDateTime.parse(now.toString()));
        attendanceModel.setStatus(Status.ONLINE);
        attendanceRepository.save(attendanceModel);
        }
    }

    public void saveTimeOut(User user) {

        LocalDateTime now = LocalDateTime.now();
        LocalTime timeOut = LocalTime.now();
        AttendanceRecord attendanceModel = attendanceRepository.findTodayAttendance(user.getEmpId());
        attendanceModel.setTimeOut(LocalTime.parse(timeOut.toString()));
        attendanceModel.setStatus(Status.OFFLINE);
        attendanceModel.setUpdatedOn(LocalDateTime.parse(now.toString()));
        attendanceRepository.save(attendanceModel);
    }


    public List<AttendanceRecord> getUserAttendance(User user) {
       /*  // Extensive debugging
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
        */
        TargetDateTime dateTime = new TargetDateTime();
        return attendanceRepository.getAttendanceRecord(user.getEmpId(), dateTime.getTargetDate());
    }


    /*public boolean isUserClockedIn(String empId) {
        List<AttendanceRecord> openRecords = attendanceRepository.findByUser_EmpIdAndTimeOutIsNull(empId);
        return !openRecords.isEmpty();
    }*/
    public boolean isUserClockedIn(User user) {
        TargetDateTime dateTime = new TargetDateTime();
        AttendanceRecord attendanceRecord = attendanceRepository.getTimeInRecord(user.getEmpId(), dateTime.getTargetDate());
        return attendanceRecord == null ? false : true;
        
    }

    public LocalTime getLatestTimeIn(User user) {

        TargetDateTime dateTime = new TargetDateTime();
        AttendanceRecord attendanceModel;

        if(dateTime.getCurrendtDateTime().isBefore(dateTime.getResetDate())){
            attendanceModel = attendanceRepository.getTimeInRecord(user.getEmpId(),dateTime.getPreviousDay());
            if(attendanceModel != null){
                return attendanceModel.getTimeIn();
            }
            else{
                attendanceModel = attendanceRepository.findByDate(user.getEmpId(),dateTime.getTargetDate());
                if(attendanceModel != null)
                {
                    LocalTime timeIn = attendanceModel.getTimeIn();
                    if (timeIn != null) {
                        return timeIn;  
                    }
                    else {
                        return null;
                    }
                }
                else{
                    return null;
                }

            }

        }
        else{
            attendanceModel = attendanceRepository.findByDate(user.getEmpId(),dateTime.getTargetDate());
            if(attendanceModel != null)
            {
                LocalTime timeIn = attendanceModel.getTimeIn();
                if (timeIn != null) {
                    return timeIn;  
                }
                else {
                    return null;
                }
            }
            else{
                return null;
            }
        }
        /*List<AttendanceRecord> userRecords = attendanceRepository.findByUser_EmpId(empId);
        
        return userRecords.stream()
            .filter(record -> record.getTimeOut() != null)
            .map(AttendanceRecord::getTimeIn)
            .max(LocalTime::compareTo)
            .orElse(null);
            */
        
    }


    public LocalTime getLatestTimeOut(User user) {
        /* 
        TargetDateTime dateTime = new TargetDateTime();
        AttendanceRecord attendanceModel = attendanceRepository.findbyDate(user.getEmpId(),dateTime.getBeforeDate());
        if(attendanceModel == null)
        {
            return null;
        }
        LocalTime timeOut = attendanceModel.getTimeOut();
                        
        if (timeOut != null) {
            return timeOut;  
        }
        return null;
        */

        TargetDateTime dateTime = new TargetDateTime();
        AttendanceRecord attendanceModel;

        if(dateTime.getCurrendtDateTime().isBefore(dateTime.getResetDate())){
            attendanceModel = attendanceRepository.getTimeInRecord(user.getEmpId(),dateTime.getPreviousDay());
            if(attendanceModel != null){
                return attendanceModel.getTimeOut();
            }
            else{
                attendanceModel = attendanceRepository.findByDate(user.getEmpId(),dateTime.getTargetDate());
                if(attendanceModel != null)
                {
                    LocalTime timeOut = attendanceModel.getTimeOut();
                    if (timeOut != null) {
                        return timeOut;  
                    }
                    else {
                        return null;
                    }
                }
                else{
                    return null;
                }

            }

        }
        else{
            attendanceModel = attendanceRepository.findByDate(user.getEmpId(),dateTime.getTargetDate());
            if(attendanceModel != null)
            {
                LocalTime timeOut = attendanceModel.getTimeOut();
                if (timeOut != null) {
                    return timeOut;  
                }
                else {
                    return null;
                }
            }
            else{
                return null;
            }
        }
    }

    @Transactional
    public AttendanceRecord saveLeave(LeaveModel leaveModel , User user) {

        AttendanceRecord attendanceRecord = attendanceRepository.findByDate(user.getEmpId(), leaveModel.getLeaveDate());
        
        if (attendanceRecord == null){
            AttendanceRecord attendanceModel =  new AttendanceRecord();
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            leaveRepository.save(leaveModel);
            attendanceModel.setLeaveModel(leaveModel);
            attendanceModel.setUser(user);
            attendanceModel.setDate(leaveModel.getLeaveDate());
            attendanceModel.setCreatedOn(LocalDateTime.parse(now.toString()));
            attendanceModel.setUpdatedOn(LocalDateTime.parse(now.toString()));
            attendanceModel.setStatus(Status.ON_LEAVE);
            attendanceModel.setRemarks(leaveModel.getLeaveType() + " leave " + leaveModel.getLeaveDuration() + "; reason: "+leaveModel.getRemarks());
            return attendanceRepository.save(attendanceModel);
        }
        else {
            leaveRepository.save(leaveModel);
            attendanceRecord.setLeaveModel(leaveModel);
            attendanceRecord.setRemarks((leaveModel.getRemarks() == null?leaveModel.getRemarks()+"":leaveModel.getRemarks()+"; ") 
                                        +leaveModel.getLeaveType() + " leave " + leaveModel.getLeaveDuration());
            if(leaveModel.getLeaveDuration()=="Am"){
               attendanceRecord.setStatus(Status.ONLINE);
               return attendanceRepository.save(attendanceRecord);

            }
            else{
            attendanceRecord.setStatus(Status.ON_LEAVE);
            return attendanceRepository.save(attendanceRecord);
            }
        }
    }
    @Transactional
    public AttendanceRecord deleteAttendance(Long id, User user) {
        
            AttendanceRecord attendanceModel = attendanceRepository.findByAttendanceId(id);
            if (attendanceModel != null) {
                if(attendanceModel.getLeaveModel() != null)
                {   
                    LeaveModel leaveModel = leaveRepository.findByLeaveId(attendanceModel.getLeaveModel().getLeaveId());
                    leaveModel.setDelFlag(1);
                    leaveModel.setDeletedById(user.getEmpId());
                    leaveRepository.save(leaveModel);
                    attendanceModel.setDelFlag(1);
                    attendanceModel.setDeletedById(user.getEmpId());
                    return attendanceRepository.save(attendanceModel);
                }
                else{
                    attendanceModel.setDelFlag(1);
                    attendanceModel.setDeletedById(user.getEmpId());
                    return attendanceRepository.save(attendanceModel);
                }
                
            } else {
                throw new RuntimeException("Attendance record not found with ID: " + id);
            }
       
    }

    @Transactional
    public List<AttendanceRecord> getAttendanceRecordByDate(User user, LocalDate startDate, LocalDate endDate) {
        List<AttendanceRecord> attendanceRecords = attendanceRepository.getAttendanceRecordByDate(user.getEmpId(), startDate, endDate);
        return attendanceRecords;
    }

    @Transactional
    public List<AttendanceRecord> getAttendanceRecordByMembersAndDate(String empId, LocalDate startDate, LocalDate endDate) {
        List<AttendanceRecord> attendanceRecords = attendanceRepository.getAttendanceRecordByDate(empId, startDate, endDate);
        return attendanceRecords;
    }
    
    @Transactional
    public AttendanceRecord updateAttendance(Long id, User user, AttendanceDto attendanceDto) {

        LocalDateTime now = LocalDateTime.now();

        AttendanceRecord attendanceModel = attendanceRepository.findByAttendanceId(id);
        if (attendanceModel != null) {

                String fullName = user.getPersonalInfo().getFirstName() + " " + user.getPersonalInfo().getLastName();
                attendanceModel.setEditedByName(fullName);
                attendanceModel.setEditedById(user.getEmpId());
                attendanceModel.setEditedByRole(user.getRole().getRoleShName());;
                attendanceModel.setUpdatedOn(LocalDateTime.parse(now.toString()));
                attendanceModel.setTimeIn(LocalTime.parse(attendanceDto.getTimeIn()));
                attendanceModel.setTimeOut(LocalTime.parse(attendanceDto.getTimeOut()));
                attendanceModel.setRemarks(attendanceDto.getRemarks());
                return attendanceRepository.save(attendanceModel);
                
        } else {
            throw new RuntimeException("Attendance record not found with ID: " + id);
        }
       
    }
    // Pagination
    // public Page<AttendanceRecord> getUserAttendancePaginated(User user, int page, int size) {
    //     Pageable pageable = PageRequest.of(page, size);
    //     return attendanceRepository.findByUser_EmpId(user.getEmpId(), pageable);
    // }

    // public Page<AttendanceRecord> getUserAttendancePaginatedByDate(User user, LocalDate startDate, LocalDate endDate, int page, int size) {
    //     Pageable pageable = PageRequest.of(page, size);
    //     return attendanceRepository.findByUser_EmpIdAndDateBetween(user.getEmpId(), startDate, endDate, pageable);
    // }
    public Page<AttendanceRecord> getUserAttendancePaginated(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return attendanceRepository.findByUser_EmpId(user.getEmpId(), pageable);
    }
    
    
    public Page<AttendanceRecord> getUserAttendancePaginatedByDate(User user, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return attendanceRepository.findByUser_EmpIdAndDateBetween(user.getEmpId(), startDate, endDate, pageable);
    }
    
    public Page<AttendanceRecord> getAttendanceRecordPaginatedByMember(String empId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return attendanceRepository.findByUser_EmpId(empId, pageable);
    }
    
    public Page<AttendanceRecord> getAttendanceRecordPaginatedByMemberAndDate(String empId, LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return attendanceRepository.findByUser_EmpIdAndDateBetween(empId, startDate, endDate, pageable);
    }
}
// ! End