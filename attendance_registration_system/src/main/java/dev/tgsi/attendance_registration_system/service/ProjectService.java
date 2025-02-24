package dev.tgsi.attendance_registration_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.tgsi.attendance_registration_system.models.ProjectManagerModel;
import dev.tgsi.attendance_registration_system.models.ProjectModel;
import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.models.UserProjectModel;
import dev.tgsi.attendance_registration_system.repository.ProjectManagerRepository;
import dev.tgsi.attendance_registration_system.repository.UserProjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {


    @Autowired
    private ProjectManagerRepository projectManagerRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    public List<ProjectManagerModel> getProjectIds(User user) {
       return projectManagerRepository.findByUser(user);
    }

    //this will be used as the project name
    public List<ProjectModel> projectList(User user) {
        
        List<ProjectModel> projectModels = new ArrayList<>();
        List<ProjectManagerModel> projectManagerModels = getProjectIds(user);

        for (ProjectManagerModel projectManagerModel : projectManagerModels) {
            int id = projectManagerModel.getProject().getProjectId();
            String projectName = projectManagerModel.getProject().getProjectName();
            projectModels.add(new ProjectModel(id,projectName));
        }
        return projectModels;
    }

    public List<User> getMembersByProject(int projectId){
        
        List<UserProjectModel> userProjectModels = userProjectRepository.findByProject_ProjectId(projectId);

        return userProjectModels.stream()
                .map(UserProjectModel::getUser)
                .collect(Collectors.toList());

    }
}



