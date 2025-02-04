package dev.tgsi.attendance_registration_system.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "tbl_user", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long epm_id;
	
	private String username;
	private String password;
	private int role_id;
	
	
	public User() {
		super();
	}

	public User(String username, String password, int role_id) {
		
		this.username = username;
		this.password = password;
		this.role_id = role_id;
		
	}

	public Long getId() {
		return epm_id;
	}

	public void setId(Long emp_id) {
		this.epm_id = emp_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRole() {
		return role_id;
	}

	public void setRole(int role_id) {
		this.role_id = role_id;
	}

	
	
	
	
	
	
	
	
	
	

}
