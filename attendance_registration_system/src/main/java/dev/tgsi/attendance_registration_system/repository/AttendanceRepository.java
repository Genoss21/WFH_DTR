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

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.timeIn IS NOT NULL AND a.timeOut IS NULL AND a.delFlag = 0")
    // *Get the attendance record for the given employee ID and date,
    // *assuming that the employee has already clocked in (timeIn is not null)
    // *and has not yet clocked out (timeOut is null).
    AttendanceRecord findTodayAttendance(String empId);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date <= :date AND a.timeIn IS NOT NULL AND a.timeOut IS NULL AND a.delFlag = 0")
    // *Get the attendance record for the given employee ID and date,
    // *assuming that the employee has already clocked in (timeIn is not null)
    // *and has not yet clocked out (timeOut is null).
    AttendanceRecord getTimeInRecord(String empId, LocalDate date);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date = :date AND a.delFlag = 0")
    // *Get the attendance record for the given employee ID and date.
    AttendanceRecord findByDate(String empId, LocalDate date);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date <= :date AND a.delFlag = 0 ORDER BY a.date DESC")
    // *Get all attendance records for the given employee ID and dates
    // *in descending order.
    List<AttendanceRecord> getAttendanceRecord(String empId, LocalDate date);

    AttendanceRecord findByAttendanceId(Long attendanceId);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date >= :startDate AND a.date <= :endDate AND a.delFlag = 0 ORDER BY a.date DESC " )
    // *Get all attendance records for the given employee ID and
    // *date range in descending order.
    List<AttendanceRecord> getAttendanceRecordByDate(String empId, LocalDate startDate, LocalDate endDate);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.delFlag = 0 ORDER BY a.date DESC")
    List<AttendanceRecord> getAttendanceRecordByMember(String empId);
    // * For pagination
    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.delFlag = 0 ORDER BY a.date DESC")
    // *Get all attendance records for the given employee ID
    // *in descending order.
    Page<AttendanceRecord> findByUser_EmpId(String empId, Pageable pageable);

    @Query("SELECT a FROM AttendanceRecord a WHERE a.user.empId = :empId AND a.date BETWEEN :startDate AND :endDate AND a.delFlag = 0 ORDER BY a.date DESC")
    // *date range in descending order.
    // *Get all attendance records for the given employee ID and
    Page<AttendanceRecord> findByUser_EmpIdAndDateBetween(String empId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<AttendanceRecord> findByUser(User user, Pageable pageable);

    Page<AttendanceRecord> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate, Pageable pageable);
}  