package dev.tgsi.attendance_registration_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.tgsi.attendance_registration_system.models.ProjectModel;
import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.repository.ProjectRepository;

import java.util.List;
import java.util.Set;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public List<ProjectModel> getAllProjects() {
        return projectRepository.findAll();
    }

    public Set<User> getProjectMembers(Integer projectId) {
        ProjectModel project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));
        return project.getMembers();
    }
}



