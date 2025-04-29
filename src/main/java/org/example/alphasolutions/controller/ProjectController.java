package org.example.alphasolutions.controller;

import org.example.alphasolutions.service.ProjectService;
import org.springframework.stereotype.Controller;

@Controller
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
}
