package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.enums.TaskStatus;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.service.EmployeeService;
import org.example.alphasolutions.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProjectController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;

    public ProjectController(ProjectService projectService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    @GetMapping("/projects")
    public String showProjects(Model model, HttpSession session) {
        if (session.getAttribute("employee") == null) {
            return "redirect:/login";
        }

        String role = (String) session.getAttribute("role");
        Integer employeeId = (Integer) session.getAttribute("employeeId");

        List<Project> projects;

        if (role.equals("ADMIN") || role.equals("PROJECT_MANAGER")) {
            projects = projectService.findAllProjects();
        } else {
            projects = projectService.findProjectsByEmployeeId(employeeId);
        }

        model.addAttribute("projects", projects);
        model.addAttribute("role", role);

        return "projects";
    }

    @GetMapping("/projects/addProject")
    public String addProjectToDatabase(Model model) {
        Project newProject = new Project();

        model.addAttribute("newProject", newProject);
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("managers", employeeService.gerAllManagers());
        return "addProject";
    }

    @PostMapping("/projects/saveProject")
    public String saveProjectToDatabase(@ModelAttribute("newProject") Project newProject) {
        int projectId = projectService.addProjectToDB(newProject);
        int managerId = newProject.getProjectManagerId();

        projectService.assignEmployeeToProject(managerId, projectId);

        return "redirect:/projects";
    }
}
