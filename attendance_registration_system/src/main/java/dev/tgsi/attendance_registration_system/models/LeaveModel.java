package dev.tgsi.attendance_registration_system.models;

import java.time.LocalDate;

import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Transactional
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_leave")
public class LeaveModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id", nullable = false)
    private Integer leaveId;

    @Column(name = "leave_date")
    private LocalDate leaveDate;

    @Column(name = "leave_type")
    private String leaveType;

    @Column(name = "leave_duration")
    private String leaveDuration;
    
    @Column(name = "remarks")
    private String remarks;

    @Column(name = "del_flag")
    private int delFlag;

    @Column(name = "deleted_by_id")
    private String deletedById;

    public LeaveModel(LocalDate leaveDate, String leaveType, String leaveDuration, String remarks, int delFlag, String deletedById) {
        this.leaveDate = leaveDate;
        this.leaveType = leaveType;
        this.leaveDuration = leaveDuration;
        this.remarks = remarks;
        this.delFlag = delFlag;
        this.deletedById = deletedById;
    }

}