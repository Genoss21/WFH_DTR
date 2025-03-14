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

    @Column(name = "title")
    private String title;

    @Column(name = "role_sh_name")
    private String roleShName;

    @Column(name = "role_user_level")
    private Integer roleUserLevel;

    public RoleModel(String title, String roleShName, Integer roleUserLevel) {
        this.title = title;
        this.roleShName = roleShName;
        this.roleUserLevel = roleUserLevel;
    }

    @Override
    public String toString() {
        return "RoleModel{" +
                "roleId=" + roleId +
                ", title='" + title + '\'' +
                ", roleShName='" + roleShName + '\'' +
                ", roleUserLevel=" + roleUserLevel +
                '}';
    }
}
