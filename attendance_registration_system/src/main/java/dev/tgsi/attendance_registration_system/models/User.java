package dev.tgsi.attendance_registration_system.models;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "emp_id")
    private Long empId;

    @Column(name = "username", nullable = false, unique = true, length = 100)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "role_id", nullable = false) 
    private RoleModel role; 

    
    public User(String username, String password, RoleModel role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
