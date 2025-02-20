package dev.tgsi.attendance_registration_system.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import dev.tgsi.attendance_registration_system.repository.TimeLogRepository;

@Service
public class TimeLogService {

    private final TimeLogRepository timeLogRepository;

    public TimeLogService(TimeLogRepository timeLogRepository) {
        this.timeLogRepository = timeLogRepository;
    }

    public AttendanceRecord updateTimeLog(Long attendanceId, LocalTime timeIn, LocalTime timeOut, String remarks) {
        // Retrieve the existing record (add your own exception handling as needed)
        AttendanceRecord record = timeLogRepository.findById(attendanceId)
                .orElseThrow(() -> new RuntimeException("Record not found for id " + attendanceId));

        // Update the record's fields
        record.setTimeIn(timeIn);
        record.setTimeOut(timeOut);
        record.setRemarks(remarks);
        record.setUpdatedOn(LocalDateTime.now());
        
        // Additional fields (e.g., editedBy) can be updated here if required

        // Save and return the updated record
        return timeLogRepository.save(record);
    }
    
    /**
     * Returns an attendance record for editing.
     * <p>
     * This example implementation attempts to fetch an existing record based on a criteria.
     * You may need to adjust this logic to fetch the record for the currently logged-in user
     * or based on another parameter. If no record is found, a new AttendanceRecord is returned.
     * </p>
     */
    public AttendanceRecord findRecordForEditing() {
        // Example logic: try to find the first record by creation date.
        // Adjust this to suit your application's needs.
        Optional<AttendanceRecord> optionalRecord = timeLogRepository.findFirstByOrderByCreatedOnAsc();
        return optionalRecord.orElse(new AttendanceRecord());
    }
}
