// ! Update 02/11/2025

package dev.tgsi.attendance_registration_system.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.tgsi.attendance_registration_system.models.AttendanceRecord;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {

    List<AttendanceRecord> findByUser_EmpId(String empId);

    List<AttendanceRecord> findByUser_EmpIdAndTimeOutIsNull(String empId);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date <= :date AND a.timeIn IS NOT NULL AND a.timeOut IS NULL AND a.delFlag = 0")
    AttendanceRecord findTodayAttendance(String empId, LocalDate date);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date <= :date AND a.date >= :beforeDate AND a.delFlag = 0")
    AttendanceRecord findbyDate(String empId, LocalDate date, LocalDate beforeDate);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date <= :date AND a.delFlag = 0 ORDER BY a.date DESC")
    List<AttendanceRecord> getAttendanceRecord(String empId, LocalDate date);

    AttendanceRecord findByAttendanceId(Long attendanceId);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date >= :startDate AND a.date <= :endDate AND a.delFlag = 0 ORDER BY a.date DESC " )
    List<AttendanceRecord> getAttendanceRecordByDate(String empId, LocalDate startDate, LocalDate endDate);
    
} 
// ! End of Update  