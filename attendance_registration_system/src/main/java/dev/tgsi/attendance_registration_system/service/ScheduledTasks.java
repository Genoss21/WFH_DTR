package dev.tgsi.attendance_registration_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dev.tgsi.attendance_registration_system.dto.TargetDateTime;
import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.repository.AttendanceRepository;
import dev.tgsi.attendance_registration_system.repository.UserRepository;

@Service
public class ScheduledTasks {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private CredentialService credentialService;


    @Scheduled(cron = "0 0 6 * * ?") 
    public void myDailyFunction() {

        System.out.println("This will automatically run everyday to reset the DTR");

        List<CredentialService.UserCredentials> credentialsList = credentialService.getAllCredentials();

        for (CredentialService.UserCredentials credentials : credentialsList) {

            TargetDateTime dateTime = new TargetDateTime();

            User user = userRepository.findByUsername(credentials.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
            // Check if the user has already recorded his attendance for today
            AttendanceRecord attendanceRecord = attendanceRepository.getTimeInRecord(user.getEmpId(),dateTime.getDateNow());
            if(attendanceRecord != null){

                // if the user has already recorded his attendance for today
                // and the record is for the previous day, then save the time out
                // and reset the DTR
                if(attendanceRecord.getDate().isEqual(dateTime.getPreviousDay())){
                attendanceService.saveTimeOut(user);
                System.out.println("Response for user " + credentials.getUsername() + ": done");
                System.out.println("automatically timeout and reset the DTR");
                }
                else{
                    System.out.println("there is no attendance record to reset");
                }

            }
            else{
                System.out.println("there is no attendance record to reset");
            }
            
        }

    }
}
