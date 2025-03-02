package dev.tgsi.attendance_registration_system.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.models.AttendanceRecord.Status;
import dev.tgsi.attendance_registration_system.dto.TargetDateTime;
import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import dev.tgsi.attendance_registration_system.models.LeaveModel;
import dev.tgsi.attendance_registration_system.repository.AttendanceRepository;
import dev.tgsi.attendance_registration_system.repository.LeaveRepository;
import jakarta.transaction.Transactional;

// Pagination
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    public Map<String, String> saveTimeIn(User user) {
        TargetDateTime dateTime = new TargetDateTime();
        AttendanceRecord attendanceRecord = attendanceRepository.findByDate(user.getEmpId(),dateTime.getDateNow());


        Map<String, String> response = new HashMap<>();
        
        if(attendanceRecord != null)
        {
            if(attendanceRecord.getStatus() == Status.ONLINE)
            {
                System.out.println("Already Login");
                response.put("title", "You are online!");
                response.put("message", "Already time in");
                response.put("status", "success");

            }else if(attendanceRecord.getStatus() == Status.ON_LEAVE)
            {
                attendanceRecord.setStatus(Status.ONLINE);
                attendanceRecord.setTimeIn(dateTime.getTimeNow());
                attendanceRecord.setUpdatedOn(dateTime.getCurrentDateTime());
                attendanceRepository.save(attendanceRecord);
                
                response.put("title", "Good Day!");
                response.put("message", "Time-in Successfully");
                response.put("status", "success");
            }
            else
            {   
                System.out.println("User is offline");
                response.put("title", "Error");
                response.put("message", "You have already rendered your time");
                response.put("status", "error");
            }
        }
        else
        {
        AttendanceRecord attendanceModel = new AttendanceRecord();
        attendanceModel.setUser(user);
        attendanceModel.setDate(dateTime.getDateNow());
        attendanceModel.setTimeIn(dateTime.getTimeNow());
        attendanceModel.setCreatedOn(dateTime.getCurrentDateTime());
        attendanceModel.setUpdatedOn(dateTime.getCurrentDateTime());
        attendanceModel.setStatus(Status.ONLINE);
        attendanceRepository.save(attendanceModel);
        response.put("title", "Good Day!");
        response.put("message", "Time-in Successfully");
        response.put("status", "success");
        }

        return response;
    }

    public void saveTimeOut(User user) {

        TargetDateTime dateTime = new TargetDateTime();
        AttendanceRecord attendanceModel = attendanceRepository.findTodayAttendance(user.getEmpId());
        attendanceModel.setTimeOut(dateTime.getTimeNow());
        attendanceModel.setStatus(Status.OFFLINE);
        attendanceModel.setUpdatedOn(dateTime.getCurrentDateTime());
        attendanceRepository.save(attendanceModel);
    }


    public List<AttendanceRecord> getUserAttendance(User user) {
        TargetDateTime dateTime = new TargetDateTime();
        return attendanceRepository.getAttendanceRecord(user.getEmpId(), dateTime.getDateNow());
    }

    public LocalTime getLatestTimeIn(User user) {

        TargetDateTime dateTime = new TargetDateTime();
        AttendanceRecord attendanceModel;

        if(dateTime.getCurrentDateTime().isBefore(dateTime.getResetDate())){
            attendanceModel = attendanceRepository.getTimeInRecord(user.getEmpId(),dateTime.getPreviousDay());
            if(attendanceModel != null){
                return attendanceModel.getTimeIn();
            }
            else{
                attendanceModel = attendanceRepository.findByDate(user.getEmpId(),dateTime.getDateNow());
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
            attendanceModel = attendanceRepository.findByDate(user.getEmpId(),dateTime.getDateNow());
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


    public LocalTime getLatestTimeOut(User user) {
      
        TargetDateTime dateTime = new TargetDateTime();
        AttendanceRecord attendanceModel;

        if(dateTime.getCurrentDateTime().isBefore(dateTime.getResetDate())){
            attendanceModel = attendanceRepository.getTimeInRecord(user.getEmpId(),dateTime.getPreviousDay());
            if(attendanceModel != null){
                return attendanceModel.getTimeOut();
            }
            else{
                attendanceModel = attendanceRepository.findByDate(user.getEmpId(),dateTime.getDateNow());
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
            attendanceModel = attendanceRepository.findByDate(user.getEmpId(),dateTime.getDateNow());
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

        TargetDateTime dateTime = new TargetDateTime();
        AttendanceRecord attendanceRecord = attendanceRepository.findByDate(user.getEmpId(), leaveModel.getLeaveDate());
        
        if (attendanceRecord == null){
            AttendanceRecord attendanceModel =  new AttendanceRecord();   
            leaveRepository.save(leaveModel);
            attendanceModel.setLeaveModel(leaveModel);
            attendanceModel.setUser(user);
            attendanceModel.setDate(leaveModel.getLeaveDate());
            attendanceModel.setCreatedOn(dateTime.getCurrentDateTime());
            attendanceModel.setUpdatedOn(dateTime.getCurrentDateTime());
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

            TargetDateTime dateTime = new TargetDateTime();
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
                    attendanceModel.setUpdatedOn(dateTime.getCurrentDateTime());
                    return attendanceRepository.save(attendanceModel);
                }
                else{
                    attendanceModel.setDelFlag(1);
                    attendanceModel.setDeletedById(user.getEmpId());
                    attendanceModel.setUpdatedOn(dateTime.getCurrentDateTime());
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
    public AttendanceRecord updateAttendance(Long id, User user, String timeInResult, String timeOutResult, String remarks) {

        TargetDateTime dateTime = new TargetDateTime();
        LocalTime timeIn =  null;
        LocalTime timeOut = null;
        if(timeInResult!=""){
            timeIn = LocalTime.parse(timeInResult);
        }
        if(timeOutResult!=""){
            timeOut = LocalTime.parse(timeOutResult);
        }
        AttendanceRecord attendanceModel = attendanceRepository.findByAttendanceId(id);
        if (attendanceModel != null) {

                String fullName = user.getPersonalInfo().getFirstName() + " " + user.getPersonalInfo().getLastName();
                attendanceModel.setEditedByName(fullName);
                attendanceModel.setEditedById(user.getEmpId());
                attendanceModel.setEditedByRole(user.getRole().getRoleShName());;
                attendanceModel.setUpdatedOn(dateTime.getCurrentDateTime());
                attendanceModel.setTimeIn(timeIn);
                attendanceModel.setTimeOut(timeOut);
                attendanceModel.setRemarks(remarks);
                return attendanceRepository.save(attendanceModel);
                
        } else {
            throw new RuntimeException("Attendance record not found with ID: " + id);
        }
       
    }
    // Pagination
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