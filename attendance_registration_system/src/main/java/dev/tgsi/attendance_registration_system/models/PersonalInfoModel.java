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

    @Column(name = "fname")
    private String firstName;

    @Column(name = "lname")
    private String lastName;

    @Column(name = "mname")
    private String midName;

    @Column(name = "email")
    private String email;

    @Column(name = "profile_picture")
    private String profilePicture; // Store profile image URL or path

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emp_id")
    @JsonBackReference
    private User user;

    /**
     * Returns initials (First Letter of First Name + First Letter of Last Name).
     */
    public String getInitials() {
        if (firstName != null && lastName != null) {
            return (firstName.substring(0, 1) + lastName.substring(0, 1)).toUpperCase();
        }
        return "U"; // Default initial if fname or lname is missing
    }

    @Override
    public String toString() {
        return "PersonalInfoModel{" +
                "personalInfoId=" + personalInfoId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", midName='" + midName + '\'' +
                ", email='" + email + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                '}';
    }
}
