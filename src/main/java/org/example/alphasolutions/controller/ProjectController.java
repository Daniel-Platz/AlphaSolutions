package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.service.EmployeeService;
import org.example.alphasolutions.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class ProjectController extends BaseController {

    private final ProjectService projectService;
    private final EmployeeService employeeService;

    public ProjectController(ProjectService projectService, EmployeeService employeeService) {
        this.projectService = projectService;
        this.employeeService = employeeService;
    }

    @GetMapping("")
    public String showProjects(Model model, HttpSession session) {
        return showFilteredProjects(null, false, model, session);
    }

    @GetMapping("/archived")
    public String showArchivedProjects(Model model, HttpSession session){
        return showFilteredProjects(ProjectStatus.ARCHIVED, true, model, session);
    }

    private String showFilteredProjects(ProjectStatus statusFilter, Boolean archivedView, Model model, HttpSession session){
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        String role = (String) session.getAttribute("role");
        Integer employeeId = (Integer) session.getAttribute("employeeId");

        List<Project> projects;

        if (role.equals("ADMIN") || role.equals("PROJECT_MANAGER")) {
            projects = projectService.findAllProjects(statusFilter);
        } else {
            projects = projectService.findProjectsByEmployeeId(employeeId);
        }

        for (Project project : projects)
        {
            project.setProjectActualHours(projectService.calculateProjectActualHours(project.getProjectId()));
        }

        Map<ProjectStatus, Long> statusCounts = projects.stream()
                .collect(Collectors.groupingBy(Project::getProjectStatus, Collectors.counting()));

        model.addAttribute("projects", projects);
        model.addAttribute("role", role);
        model.addAttribute("archivedView", archivedView);
        model.addAttribute("statusCounts", statusCounts);

        return "dashboard";
    }

    @GetMapping("/{projectId}/projectOverview")
    public String showProjectDetails (@PathVariable int projectId, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        Project project = projectService.findProjectById(projectId);
        if (project==null) {
            return "redirect:/dashboard";
        }

        List<SubProject> subProjects = projectService.findSubProjectsByProjectId(projectId);
        List<Employee> assignedEmployees = projectService.findAssignedEmployeesByProjectId(projectId);
        List<Employee> allEmployees = employeeService.getAllEmployees();

        for (SubProject subProject : subProjects)
        {
            subProject.setSubProjectActualHours(projectService.calculateSubProjectActualHours(subProject.getSubProjectId()));
        }

        List<Employee> availableEmployees = allEmployees.stream()
                .filter(emp -> assignedEmployees.stream().noneMatch(ae -> ae.getEmployeeId() == emp.getEmployeeId()))
                .toList();

        model.addAttribute("project", project);
        model.addAttribute("subProjects", subProjects);
        model.addAttribute("assignedEmployees", assignedEmployees);
        model.addAttribute("availableEmployees", availableEmployees);
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("role", session.getAttribute("role"));

        return "projectOverview";
    }

    @GetMapping("/addProject")
    public String addProjectToDatabase(Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        Project newProject = new Project();

        model.addAttribute("newProject", newProject);
        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("managers", employeeService.getAllManagers());
        return "addProject";
    }

    @PostMapping("/saveProject")
    public String saveProjectToDatabase(@ModelAttribute("newProject") Project newProject, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        int projectId = projectService.addProjectToDB(newProject);
        int managerId = newProject.getManagerId();

        projectService.assignEmployeeToProject(managerId, projectId);

        return "redirect:/dashboard";
    }

    @PostMapping("/{projectId}/deleteProject")
    public String deleteProjectFromDB(@PathVariable("projectId") int projectId){
        projectService.deleteProjectFromDB(projectId);
        return "redirect:/dashboard";
    }

    @GetMapping("/{projectId}/editProject")
    public String editProject(@PathVariable("projectId") int projectId, Model model){
        Project project = projectService.findProjectById(projectId);

        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("managers", employeeService.getAllManagers());
        model.addAttribute("project", project);
        model.addAttribute("oldManagerId", project.getManagerId());
        return "editProject";
    }

    @PostMapping("/{projectId}/editProject")
    public String saveEditProject(@PathVariable("projectId") int projectId,
                                  @RequestParam("oldManagerId") int oldManagerId,
                                  @ModelAttribute Project project){
        project.setProjectId(projectId);
        projectService.updateProject(project, oldManagerId);

        return "redirect:/dashboard";
    }

    @PostMapping("/{projectId}/employees/add")
    public String addEmployeeToProject(@PathVariable int projectId,
                                       @RequestParam("employeeId") int employeeId,
                                       HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        projectService.assignEmployeeToProject(employeeId, projectId);

        return "redirect:/dashboard/" + projectId + "/projectOverview";
    }

    @PostMapping("/{projectId}/employees/{employeeId}/remove")
    public String removeEmployeeFromProject(@PathVariable int projectId,
                                            @PathVariable int employeeId,
                                            HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        projectService.removeEmployeeFromProject(employeeId, projectId);
        return "redirect:/dashboard/" + projectId + "/projectOverview";
    }
}
