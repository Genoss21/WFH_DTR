package dev.tgsi.attendance_registration_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tgsi.attendance_registration_system.models.UserProjectModel;

import java.util.List;

@Repository
public interface UserProjectRepository extends JpaRepository<UserProjectModel, Integer> {
    List<UserProjectModel> findByProjectProjectId(Integer projectId);
}
