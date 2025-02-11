package dev.tgsi.attendance_registration_system.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "tbl_personal_info")
@Data
@NoArgsConstructor
public class PersonalInfoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    private Integer personalInfoId;

    @Column(name = "emp_id", length = 50)
    private String empId;

    @Column(name = "fname", nullable = false, length = 150)
    private String firstName;

    @Column(name = "lname", nullable = false, length = 150)
    private String lastName;

    @Column(name = "mname", length = 150)
    private String midName;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emp_id", referencedColumnName = "emp_id", insertable = false, updatable = false)
    @JsonBackReference
    private User user;

    @Override
    public String toString() {
        return "PersonalInfoModel{" +
                "personalInfoId=" + personalInfoId +
                ", empId='" + empId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", midName='" + midName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
