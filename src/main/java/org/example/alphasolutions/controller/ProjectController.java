package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public String showProjects(Model model, HttpSession session) {
        if (session.getAttribute("employee")==null) {
            return "redirect:/login";
        }

        String role = (String) session.getAttribute("role");
        Integer employeeId=(Integer) session.getAttribute("employeeId");

        List<Project> projects;

        if (role.equals("ADMIN")|| role.equals("PROJECT_MANAGER")) {
            projects=projectService.findAllProjects();
        } else {
            projects = projectService.findProjectsByEmployeeId(employeeId);
        }

        model.addAttribute("projets", projects);
        model.addAttribute("role",role);

        return "projects";
    }
}
