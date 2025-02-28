package dev.tgsi.attendance_registration_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tgsi.attendance_registration_system.models.ProjectManagerModel;
import dev.tgsi.attendance_registration_system.models.User;

@Repository
public interface ProjectManagerRepository extends JpaRepository<ProjectManagerModel, Integer>{
    
    /**
     * Find all project id's handled by project managers.
     * @param user the user
     * @return the list of project managers
     */
    List<ProjectManagerModel> findByUser(User user);

}
