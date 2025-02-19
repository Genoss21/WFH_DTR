package dev.tgsi.attendance_registration_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.tgsi.attendance_registration_system.models.ProjectModel;
import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.models.UserProjectModel;
import dev.tgsi.attendance_registration_system.repository.ProjectRepository;
import dev.tgsi.attendance_registration_system.repository.UserProjectRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    public List<ProjectModel> getAllProjects() {
        return projectRepository.findAll();
    }

    public Set<User> getProjectMembers(Integer projectId) {
        // Fetch UserProject entities associated with the project
        List<UserProjectModel> userProjects = userProjectRepository.findByProjectProjectId(projectId);

        // Extract users from the UserProject entities
        return userProjects.stream()
                           .map(UserProjectModel::getUser)
                           .collect(Collectors.toSet());
    }
}




