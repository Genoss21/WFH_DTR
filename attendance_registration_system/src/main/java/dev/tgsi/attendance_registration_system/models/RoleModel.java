package dev.tgsi.attendance_registration_system.models;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_role_mst", uniqueConstraints = @UniqueConstraint(columnNames = "role_sh_name"))
public class RoleModel {

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long role_id;
	
	private String title;
	private String role_sh_name;
	

    
}
