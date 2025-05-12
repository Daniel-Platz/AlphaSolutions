package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.enums.TaskStatus;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/project/subproject/task") //TODO Skal fjernes igen...
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/addTask")
    public String addNewTask(@PathVariable int projectId, @PathVariable int subProjectId,  Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

    //TODO Rigtig path skal sættes
    @PostMapping("/{subProjectId}/delete")
    public String deleteTask(@PathVariable int subProjectId,
                             @PathVariable int taskId) {

        taskService.deleteTask(taskService.findTaskByTaskId(taskId));

        return "redirect:/project/subproject/task/" + subProjectId;

    }

    //TODO Rigtig path skal sættes
    @GetMapping("/{subProjectId}/edit")
    public String editTask(@PathVariable int taskId,
                           //@PathVariable int subProjectId,
                           Model model,
                           HttpSession session) {

        Integer employeeId = (Integer) session.getAttribute("employeeId");

        if (employeeId == null) {
            return "project";
        }

        Task task = taskService.findTaskByTaskId(taskId);

        if (task == null) {
            return ("redirect:/employee/" + employeeId);
        }

        model.addAttribute("task", task);
        return "editTask";
    }

    @GetMapping("/tasks")
    public String showTasks(Model model, HttpSession session) {
        if (session.getAttribute("employee") == null) {
            return "redirect:/login";
        }

        List<Task> tasks = taskService.findAllTasks();
        model.addAttribute("tasks", tasks);

        return "sub-projects";
    }

 */

}
