package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.enums.TaskStatus;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.service.ProjectService;
import org.example.alphasolutions.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview")
public class TaskController extends BaseController {
    private final TaskService taskService;
    private final ProjectService projectService;


    public TaskController(TaskService taskService, ProjectService projectService) {
        this.taskService = taskService;
        this.projectService = projectService;
    }

    @GetMapping("/addTask")
    public String addNewTask(@PathVariable int projectId, @PathVariable int subProjectId,  Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        Task newTask = new Task();

        newTask.setSubProjectId(subProjectId);

        model.addAttribute("newTask", newTask);
        model.addAttribute("projectId", projectId);
        model.addAttribute("subProjectId", subProjectId);
        model.addAttribute("statuses", TaskStatus.values());
        return "addTask";
    }


    @GetMapping("/{taskId}/taskOverview")
    public String showTaskOverview(@PathVariable int projectId, @PathVariable int subProjectId, @PathVariable int taskId, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        Task task = taskService.findTaskByTaskId(taskId);

        List<Employee> projectEmployees = projectService.findAssignedEmployeesByProjectId(projectId);

        List<Employee> taskEmployees = taskService.findAssignedEmployeesByTaskId(taskId);

        List<Employee> availableEmployees = new ArrayList<>();
        for (Employee projectEmp : projectEmployees) {
            boolean isAvailable = true;
            for (Employee taskEmp : taskEmployees) {
                if (projectEmp.getEmployeeId()==taskEmp.getEmployeeId()) {
                    isAvailable = false;
                    break;
                }
            }
            if (isAvailable) {
                availableEmployees.add(projectEmp);
            }
        }



        model.addAttribute("task", task);
        model.addAttribute("projectId", projectId);
        model.addAttribute("subProjectId", subProjectId);
        model.addAttribute("taskId", taskId);
        model.addAttribute("taskEmployees", taskEmployees);
        model.addAttribute("projectEmployees", projectEmployees);
        model.addAttribute("availableEmployees", availableEmployees);


        String role = (String) session.getAttribute("role");
        model.addAttribute("role", role);

        return "taskOverview";
    }


    @PostMapping("/{taskId}/deleteTask")
    public String deleteTask(@PathVariable("projectId") int projectId, @PathVariable ("subProjectId") int subProjectId,
                             @PathVariable ("taskId") int taskId) {

        taskService.deleteTask(taskId);
        return "redirect:/dashboard/" + projectId + "/projectOverview/" + subProjectId + "/subProjectOverview";
    }


    @PostMapping("/saveTask")
    public String createTask(@PathVariable int projectId, @PathVariable int subProjectId, @ModelAttribute Task task) {
        task.setSubProjectId(subProjectId);
        taskService.addNewTask(task);

        return "redirect:/dashboard/" + projectId + "/projectOverview/" + subProjectId + "/subProjectOverview";
    }

    @PostMapping("/{taskId}/updateTask")
    public String updateTask(@PathVariable int projectId, @PathVariable int subProjectId, @PathVariable int taskId, @ModelAttribute Task task) {
        task.setSubProjectId(subProjectId);
        task.setTaskId(taskId);
        taskService.editTask(task);

        return "redirect:/dashboard/" + projectId + "/projectOverview/" + subProjectId + "/subProjectOverview";
    }

    @GetMapping("/{taskId}/editTask")
    public String editTask(@PathVariable("projectId") int projectId, @PathVariable("subProjectId") int subProjectId, @PathVariable int taskId, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        Task task = taskService.findTaskByTaskId(taskId);

        model.addAttribute("projectId", projectId);
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("task", task);


        return "editTask";
    }


    @PostMapping("/{taskId}/taskOverview/addEmployee")
    public String addEmployeeToTask(@PathVariable int projectId, @PathVariable int subProjectId, @PathVariable int taskId,
                                       @RequestParam("employeeId") int employeeId,
                                       HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        taskService.assignEmployeeToTask(employeeId, taskId);

        return "redirect:/dashboard/" + projectId + "/projectOverview/" + subProjectId + "/subProjectOverview/" + taskId + "/taskOverview";
    }

    @PostMapping("/{taskId}/employees/{employeeId}/remove")
    public String removeEmployeeFromTask(@PathVariable int projectId,
                                         @PathVariable int subProjectId,
                                         @PathVariable int taskId,
                                         @PathVariable int employeeId,
                                         HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        taskService.removeEmployeeFromTask(employeeId, taskId);
        return "redirect:/dashboard/" + projectId + "/projectOverview/" + subProjectId + "/subProjectOverview/" + taskId + "/taskOverview";
    }
}
