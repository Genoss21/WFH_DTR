package dev.tgsi.attendance_registration_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tgsi.attendance_registration_system.models.AttendanceRecord;

@Repository
public interface TimeLogRepository extends JpaRepository<AttendanceRecord, Long> {
    Optional<AttendanceRecord> findFirstByOrderByCreatedOnAsc();
}
