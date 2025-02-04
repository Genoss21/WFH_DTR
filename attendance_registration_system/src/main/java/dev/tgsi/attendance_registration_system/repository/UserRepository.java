package dev.tgsi.attendance_registration_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tgsi.attendance_registration_system.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
	User findByUsername (String username);
}
