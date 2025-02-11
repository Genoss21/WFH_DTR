package dev.tgsi.attendance_registration_system.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @Column(name = "emp_id", length = 50, nullable = false)
    private String empId;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleModel role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_id", nullable = false)
    private PositionModel position;

    @Column(name = "img_src", length = 255)
    private String imgSrc;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private PersonalInfoModel personalInfo;
    
    // ! Added: 02/11/2025
    // Mapping the user for attendance records
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<AttendanceRecord> attendanceRecords;
    // ! End of added

    @Override
    public String toString() {
        return "User{" +
                "empId='" + empId + '\'' +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", position=" + position +
                ", imgSrc='" + imgSrc + '\'' +
                '}';
    }
}
