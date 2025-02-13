// !Added: 02/11/2025
package dev.tgsi.attendance_registration_system.models;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@Table(name = "tbl_attendance_records")
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;
  
    private LocalDateTime timeIn;
    private LocalDateTime timeOut;
    private String remarks;

    // Foreign key for emp_id
    @ManyToOne
    @JoinColumn(name = "emp_id")
    private User user;

    
}
// !End file