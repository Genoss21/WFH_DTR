package dev.tgsi.attendance_registration_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.tgsi.attendance_registration_system.models.User;
import dev.tgsi.attendance_registration_system.service.ProjectService;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // * For Admin to View Project Members
    @GetMapping("/{projectId}/members")
    public List<User> getMembersByProject(@PathVariable int projectId) {
        return projectService.getMembersByProject(projectId);
    }
}

