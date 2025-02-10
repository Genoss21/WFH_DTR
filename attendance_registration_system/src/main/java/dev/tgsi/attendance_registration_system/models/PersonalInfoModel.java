package dev.tgsi.attendance_registration_system.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_personal_info")
@Data
@NoArgsConstructor
public class PersonalInfoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pid")
    private Integer personalInfoId;

    @Column(name = "fname", nullable = false, length = 150)
    private String firstName;

    @Column(name = "lname", nullable = false, length = 150)
    private String lastName;

    @Column(name = "mname", nullable = true, length = 150)
    private String midName;

    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emp_id", nullable = false)
    private User user;

    public PersonalInfoModel(String firstName, String lastName, String midName, String email, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.midName = midName;
        this.email = email;
        this.user = user;
    }
}
