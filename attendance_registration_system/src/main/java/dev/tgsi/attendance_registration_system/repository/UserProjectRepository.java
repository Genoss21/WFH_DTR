package dev.tgsi.attendance_registration_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tgsi.attendance_registration_system.models.UserProjectModel;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProjectModel, Integer>{

    /**
     * Find all user project models for the given project id.
     * @param projectId the project id
     * @return the list of user project models
     */
    List<UserProjectModel> findByProject_ProjectId(int projectId);
    
}
