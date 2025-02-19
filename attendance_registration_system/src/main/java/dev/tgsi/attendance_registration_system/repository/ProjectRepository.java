package dev.tgsi.attendance_registration_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tgsi.attendance_registration_system.models.ProjectModel;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectModel, Integer> {
}


