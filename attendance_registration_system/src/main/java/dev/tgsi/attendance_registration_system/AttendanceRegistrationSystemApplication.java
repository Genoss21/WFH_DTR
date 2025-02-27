package dev.tgsi.attendance_registration_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AttendanceRegistrationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceRegistrationSystemApplication.class, args);
	}

}
