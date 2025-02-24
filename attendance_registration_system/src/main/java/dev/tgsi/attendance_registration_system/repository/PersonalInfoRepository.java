package dev.tgsi.attendance_registration_system.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tgsi.attendance_registration_system.models.PersonalInfoModel;

@Repository
public interface PersonalInfoRepository extends JpaRepository<PersonalInfoModel, Integer> {
    List<PersonalInfoModel> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        String firstName, String lastName, String email);
    
} 