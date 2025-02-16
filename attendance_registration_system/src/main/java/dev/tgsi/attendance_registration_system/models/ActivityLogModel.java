package dev.tgsi.attendance_registration_system.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Transactional
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_act_log")
public class ActivityLogModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private int logId;

    @ManyToOne
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id")
    private User user;

    @Column(name = "log_desc")
    private String logDesc;

    @Column(name = "log_date")
    private LocalDate logDate;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    public ActivityLogModel(User user, String logDesc, LocalDate logDate, LocalDateTime regDate,
            LocalDateTime updateDate) {
        this.user = user;
        this.logDesc = logDesc;
        this.logDate = logDate;
        this.regDate = regDate;
        this.updateDate = updateDate;
    }

    

}