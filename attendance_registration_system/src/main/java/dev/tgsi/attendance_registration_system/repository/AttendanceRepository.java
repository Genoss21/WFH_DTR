// ! Update 02/11/2025

package dev.tgsi.attendance_registration_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tgsi.attendance_registration_system.models.AttendanceRecord;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {

    List<AttendanceRecord> findByUser_EmpId(String empId);

    List<AttendanceRecord> findByUser_EmpIdAndTimeOutIsNull(String empId);

    
} 
// ! End of Update 