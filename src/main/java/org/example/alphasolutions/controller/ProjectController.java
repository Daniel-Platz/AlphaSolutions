package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.enums.TaskStatus;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.service.EmployeeService;
import org.example.alphasolutions.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/projects/{projectId}/overview")
    public String showProjectDetails (@PathVariable int projectId, Model model, HttpSession session) {
        if (session.getAttribute("employee") == null) {
            return "redirect:/login";
        }
        Project project = projectService.findProjectById(projectId);
        if (project==null) {
            return "redirect:/projects";
        }

        List<SubProject> subProjects = projectService.findSubProjectsByProjectId(projectId);
        List<Employee> assignedEmployees = projectService.findAssignedEmployeesByProjectId(projectId);

        model.addAttribute("project", project);
        model.addAttribute("subProjects", subProjects);
        model.addAttribute("assignedEmployees", assignedEmployees);
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("role", session.getAttribute("role"));

        return "projectOverview";
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

    @PostMapping("projects/{projectId}/delete")
    public String deleteProjectFromDB(@PathVariable("projectId") int projectId){
        projectService.deleteProjectFromDB(projectId);
        return "redirect:/projects";
    }

    @GetMapping("/projects/{projectId}/edit")
    public String editProject(@PathVariable("projectId") int projectId, Model model){
        Project project = projectService.findProjectById(projectId);

        model.addAttribute("project", project);
        return "redirect:/projects";
    }
}
