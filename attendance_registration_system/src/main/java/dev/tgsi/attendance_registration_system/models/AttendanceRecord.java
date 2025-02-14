// !Added: 02/14/2025
package dev.tgsi.attendance_registration_system.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@Transactional
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_attendance_records")
public class AttendanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;
  
    // Foreign key for emp_id
    @ManyToOne
    @JoinColumn(name = "emp_id")
    private User user;

    // Foreign key for leave_id
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="leave_id")
    private LeaveModel leaveModel;

    @Column(name ="date")
    private LocalDate date;

    @Column(name ="time_in", nullable = false)
    private LocalTime timeIn;

    @Column(name ="time_out")
    private LocalTime timeOut;

    @Column(name ="created_on", updatable = false)
    private LocalDateTime createdOn;

    @Column(name ="updated_on")
    private LocalDateTime updatedOn;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name ="remarks")
    private String remarks;

    @Column(name ="del_flag", nullable = false)
    private int delFlag;

    @Column(name ="edited_by_role")
    private String editedByRole;

    @Column(name ="edited_by_name")
    private String editedByName;

    @Column(name ="edited_by_id")
    private String editedById;

    public AttendanceRecord(User user, LeaveModel leaveModel, LocalDate date, LocalTime timeIn, LocalTime timeOut, String status,
            String remarks, int delFlag, LocalDateTime createdOn, LocalDateTime updatedOn, String editedByRole, String editedByName,
            String editedById) {
        this.user = user;
        this.leaveModel = leaveModel;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.status = Status.valueOf(status);
        this.remarks = remarks;
        this.delFlag = delFlag;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.editedByRole = editedByRole;
        this.editedByName = editedByName;
        this.editedById = editedById;
    }


    public enum Status {
        ONLINE, OFFLINE, ON_LEAVE
    }
}
// !End file