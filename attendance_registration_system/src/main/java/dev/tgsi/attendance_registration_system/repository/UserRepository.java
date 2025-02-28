package dev.tgsi.attendance_registration_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tgsi.attendance_registration_system.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * Find a user by username.
     * @param username the username
     * @return the user
     * 
     * This method returns an Optional containing the user if found, otherwise an empty Optional.
     */
    Optional<User> findByUsername(String username);

    /**
     * Find a user by emp id.
     * @param empId the emp id
     * @return the user
     * 
     * This method returns an Optional containing the user if found, otherwise an empty Optional.
     */
    Optional<User> findByEmpId(String empId);

}
