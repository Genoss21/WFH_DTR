package dev.tgsi.attendance_registration_system.service;

import dev.tgsi.attendance_registration_system.models.User;

public interface ActivityLogService {
    
    void saveLog(String logDesc, User user);

}
