package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.exception.InsufficientHoursException;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.service.ProjectService;
import org.example.alphasolutions.service.SubProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/dashboard/{projectId}/projectOverview")
public class SubProjectController extends BaseController {

    private final SubProjectService subProjectService;
    private final ProjectService projectService;

    public SubProjectController(SubProjectService subProjectService, ProjectService projectService) {
        this.subProjectService = subProjectService;
        this.projectService = projectService;
    }

    @GetMapping("/{subProjectId}/subProjectOverview")
    public String showSubProjectOverview(@PathVariable int projectId, @PathVariable int subProjectId, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        List<Task> tasks = subProjectService.findTasksBySubProjectId(subProjectId);
        SubProject subProject = subProjectService.findSubProjectById(subProjectId);

        int totalEstimatedHours = subProject.getSubProjectEstimatedHours();
        int actualHours = subProjectService.calculateActualHours(subProjectId);
        int hoursUsedPercentage = subProjectService.calculateHoursUsedPercentage(subProjectId);

        model.addAttribute("subProject", subProject);

        model.addAttribute("tasks", tasks);
        model.addAttribute("projectId", projectId);
        model.addAttribute("subProjectId", subProjectId);
        model.addAttribute("totalEstimatedHours", totalEstimatedHours);
        model.addAttribute("actualHours", actualHours);
        model.addAttribute("hoursUsedPercentage", hoursUsedPercentage);

        String role = (String) session.getAttribute("role");
        model.addAttribute("role", role);

        return "subProjectOverview";
    }

    @GetMapping("/addSubproject")
    public String addNewSubProject(@PathVariable int projectId, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        SubProject newSubProject = new SubProject();

        newSubProject.setProjectId(projectId);

        model.addAttribute("newSubProject", newSubProject);
        model.addAttribute("projectId", projectId);
        model.addAttribute("statuses", ProjectStatus.values());
        return "addSubProject";
    }

    @GetMapping("/{subProjectId}/editSubProject")
    public String editProject(@PathVariable("projectId") int projectId, @PathVariable("subProjectId") int subProjectId, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        SubProject subProject = subProjectService.findSubProjectById(subProjectId);

        model.addAttribute("statuses", ProjectStatus.values());
        model.addAttribute("subProject", subProject);
        return "editSubProject";
    }

    @PostMapping("/saveSubProject")
    public String saveSubProject(@PathVariable int projectId, @ModelAttribute SubProject subProject, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }
        subProject.setProjectId(projectId);

        try {
            Project project = projectService.findProjectById(projectId);
            int projectEstimatedHours = project.getProjectEstimatedHours();

            if (subProject.getSubProjectId() > 0) {
                subProjectService.editSubProject(subProject, projectEstimatedHours);
            } else {
                subProjectService.addNewSubProject(subProject, projectEstimatedHours);
            }
            return "redirect:/dashboard/" + projectId + "/projectOverview";

        } catch (InsufficientHoursException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("statuses", ProjectStatus.values());

            if (subProject.getSubProjectId() > 0) {
                model.addAttribute("subProject", subProject);
                return "editSubProject";
            } else {
                model.addAttribute("newSubProject", subProject);
                return "addSubProject";
            }
        }
    }

    @PostMapping("/{subProjectId}/deleteSubProject")
    public String deleteSubProject(@PathVariable("projectId") int projectId, @PathVariable("subProjectId") int subProjectId) {
        subProjectService.deleteSubProject(subProjectId);
        return "redirect:/dashboard/" + projectId + "/projectOverview";
    }
}