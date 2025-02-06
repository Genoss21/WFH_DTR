package dev.tgsi.attendance_registration_system.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_role_mst")
@Data
@NoArgsConstructor
public class RoleModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "role_id") 
    private Integer roleId;
   
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "role_sh_name", nullable = false, length = 50)
    private String roleShName;

    @Column(name = "role_user_level")
    private Integer roleUserLevel;


 
    public RoleModel(String title, String roleShName, Integer roleUserLevel) {
        this.title = title;
        this.roleShName = roleShName;
        this.roleUserLevel = roleUserLevel;
    }
}
