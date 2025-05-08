package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.service.SubProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects/{projectId}")
public class SubProjectController {

    private final SubProjectService subProjectService;

    public SubProjectController(SubProjectService subProjectService) {
        this.subProjectService = subProjectService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("employee") != null;
    }

    @GetMapping("/subprojects/{subProjectId}")
    public String showSubProjectOverview(@PathVariable int projectId, @PathVariable int subProjectId, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        int totalHours = subProjectService.calculateSubProjectTotalHours(subProjectId);

        List<Task> tasks = subProjectService.findTasksBySubProjectId(subProjectId);
        model.addAttribute("tasks", tasks);
        model.addAttribute("projectId", projectId);
        model.addAttribute("subProjectId", subProjectId);
        model.addAttribute("totalHours", totalHours);

        String role = (String) session.getAttribute("role");
        model.addAttribute("role", role);

        return "subProjectOverview";
    }

    @GetMapping("/subprojects/addSubproject")
    public String addNewSubProject(@PathVariable int projectId, Model model) {
        SubProject newSubProject = new SubProject();

        newSubProject.setProjectId(projectId);

        model.addAttribute("newSubProject", newSubProject);
        model.addAttribute("projectId", projectId);
        model.addAttribute("statuses", ProjectStatus.values());
        return "addSubProject";
    }

    @PostMapping("/subprojects/saveSubproject")
    public String saveSubProject(@PathVariable int projectId, @ModelAttribute SubProject newSubProject) {
        newSubProject.setProjectId(projectId);

        return "redirect:/projects/" + projectId + "/subProjectOverview";
    }
}