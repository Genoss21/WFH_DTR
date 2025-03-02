package dev.tgsi.attendance_registration_system.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.tgsi.attendance_registration_system.dto.TargetDateTime;
import dev.tgsi.attendance_registration_system.models.ActivityLogModel;
import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.repository.ActivityLogRepository;
import dev.tgsi.attendance_registration_system.service.ActivityLogService;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    // To save activity logs
    @Override
    public void saveLog(String logDesc, User user) {
        
        TargetDateTime dateTime = new TargetDateTime();
        LocalDate logDate = dateTime.getDateNow();
        LocalDateTime now = dateTime.getCurrentDateTime();
        LocalDateTime regDate = LocalDateTime.parse(now.toString());
        LocalDateTime updateDate = LocalDateTime.parse(now.toString());
        ActivityLogModel activityLog = new ActivityLogModel(user, logDesc, logDate, regDate, updateDate);
        activityLogRepository.save(activityLog);
    }
    
}