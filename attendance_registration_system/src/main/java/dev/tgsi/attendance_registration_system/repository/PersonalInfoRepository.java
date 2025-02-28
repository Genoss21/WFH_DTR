package dev.tgsi.attendance_registration_system.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tgsi.attendance_registration_system.models.PersonalInfoModel;

@Repository
public interface PersonalInfoRepository extends JpaRepository<PersonalInfoModel, Integer> {
    /**
     * Finds PersonalInfoModel objects by searching the first name, last name and email containing the given search terms.
     * The search is case-insensitive.
     * 
     * @param firstName The first name to search for.
     * @param lastName The last name to search for.
     * @param email The email to search for.
     * @return A list of PersonalInfoModel objects matching the search criteria.
     */
    List<PersonalInfoModel> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
        String firstName, String lastName, String email);

    
} 