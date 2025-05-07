package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.service.SubProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/subprojects/{subProjectId}/tasks")
    public String showTasks (@PathVariable int subProjectId, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        int totalHours = subProjectService.calculateSubProjectTotalHours(subProjectId);

        List<Task> tasks = subProjectService.findTasksBySubProjectId(subProjectId);
        model.addAttribute("tasks", tasks);
        model.addAttribute("subProjectId", subProjectId);
        model.addAttribute("totalHours", totalHours);

        String role = (String) session.getAttribute("role");
        model.addAttribute("role", role);

        return "subProjectOverview";
    }

}
