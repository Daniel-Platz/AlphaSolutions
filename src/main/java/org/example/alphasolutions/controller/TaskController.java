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

import java.util.List;

@Controller
@RequestMapping("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview")
public class TaskController extends BaseController {
    private final TaskService taskService;


    public TaskController(TaskService taskService) {
        this.taskService = taskService;
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

        model.addAttribute("task", task);
        model.addAttribute("projectId", projectId);
        model.addAttribute("subProjectId", subProjectId);
        model.addAttribute("taskId", taskId);

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


//    @GetMapping("/tasks")
//    public String showTasks(Model model, HttpSession session) {
//        if (session.getAttribute("employee") == null) {
//            return "redirect:/login";
//        }
//
//        List<Task> tasks = taskService.findAllTasks();
//        model.addAttribute("tasks", tasks);
//
//        return "sub-projects";
//    }

/*
    @GetMapping("/{taskId}/taskOverview")
    public String showTaskOverview(@PathVariable int projectId, @PathVariable int subProjectId,
                                   @PathVariable int taskId, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        Task task = taskService.findTaskByTaskId(taskId);

        // Hent medarbejdere tilknyttet opgaven
        List<Employee> taskEmployees = taskService.findAssignedEmployeesByTaskId(taskId);

        // Forsøg at hente projekt-medarbejdere fra modellen
        @SuppressWarnings("unchecked")
        List<Employee> projectEmployees = (List<Employee>) model.getAttribute("projectEmployees");

        // Hvis der ikke findes projektmedarbejdere i modellen, redirect
        if (projectEmployees == null) {
            return "redirect:/dashboard/" + projectId + "/projectOverview/" + subProjectId + "/subProjectOverview";
        }

        // Simplere måde at finde tilgængelige medarbejdere
        List<Employee> availableEmployees = new ArrayList<>(projectEmployees);
        availableEmployees.removeAll(taskEmployees); // Fjern alle task-medarbejdere fra listen

        // Tilføj til modellen
        model.addAttribute("task", task);
        model.addAttribute("projectId", projectId);
        model.addAttribute("subProjectId", subProjectId);
        model.addAttribute("taskId", taskId);
        model.addAttribute("taskEmployees", taskEmployees);
        model.addAttribute("availableEmployees", availableEmployees);

        String role = (String) session.getAttribute("role");
        model.addAttribute("role", role);

        return "taskOverview";
    }

 */


}
