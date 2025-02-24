// ! Update 02/11/2025

package dev.tgsi.attendance_registration_system.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import dev.tgsi.attendance_registration_system.models.AttendanceRecord;
import dev.tgsi.attendance_registration_system.models.User;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceRecord, Long> {

    List<AttendanceRecord> findByUser_EmpId(String empId);

    List<AttendanceRecord> findByUser_EmpIdAndTimeOutIsNull(String empId);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date < :date AND a.timeIn IS NOT NULL AND a.timeOut IS NULL AND a.delFlag = 0")
    AttendanceRecord findTodayAttendance(String empId, LocalDate date);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date = :date AND a.delFlag = 0")
    AttendanceRecord findbyDate(String empId, LocalDate date);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date = :date AND a.delFlag = 0")
    AttendanceRecord getTodayAttendanceByDate(String empId, LocalDate date);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date < :date AND a.delFlag = 0 ORDER BY a.date DESC")
    List<AttendanceRecord> getAttendanceRecord(String empId, LocalDate date);

    AttendanceRecord findByAttendanceId(Long attendanceId);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date >= :startDate AND a.date <= :endDate AND a.delFlag = 0 ORDER BY a.date DESC " )
    List<AttendanceRecord> getAttendanceRecordByDate(String empId, LocalDate startDate, LocalDate endDate);

    // ! For pagination
    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.delFlag = 0 ORDER BY a.date DESC")
    Page<AttendanceRecord> findByUser_EmpId(String empId, Pageable pageable);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date BETWEEN :startDate AND :endDate AND a.delFlag = 0 ORDER BY a.date DESC")
    Page<AttendanceRecord> findByUser_EmpIdAndDateBetween(String empId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<AttendanceRecord> findByUser(User user, Pageable pageable);

    Page<AttendanceRecord> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate, Pageable pageable);
} 
// ! End of Update  