package org.example.alphasolutions.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphasolutions.enums.TaskStatus;
import org.example.alphasolutions.exception.Task.MissingTaskNameException;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
@RequestMapping("/project/subproject/task") //TODO Skal fjernes igen...
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //TODO Rigtig path skal sættes
    //TODO lav en try/catch (??) som fanger og fejler hvis man forsøger at oprette en task uden et navn
    @PostMapping("/{subProjectId}/add")
    public String addTask(@PathVariable int subProjectId,
                          @RequestParam("taskName") String taskName,
                          @RequestParam(value = "taskDescription", required = false) String taskDescription,
                          @RequestParam(value = "taskStartDate", required = false) Date taskStartDate,
                          @RequestParam(value = "taskEndDate", required = false) Date taskEndDate,
                          @RequestParam(value = "taskEstimatedHours", required = false) Integer taskEstimatedHours,
                          @RequestParam(value = "taskStatus", required = false) TaskStatus taskStatus, RedirectAttributes redirectAttributes) {

        Task newTask = new Task();
        newTask.setTaskName(taskName);

        if (taskName == null || taskName.trim().isEmpty()) {
            throw new MissingTaskNameException(); //Throws exception if you try to add a new task without a name
        }

        if (taskDescription != null && !taskDescription.trim().isEmpty()) {
            newTask.setTaskDescription(taskDescription);
        }

        if (taskStartDate != null) {
            newTask.setTaskStartDate(taskStartDate);
        }
        if (taskEndDate != null) {
            newTask.setTaskEndDate(taskEndDate);
        }
        if (taskEstimatedHours != null) {
            newTask.setTaskEstimatedHours(taskEstimatedHours);
        }
        if (taskStatus != null) {
            newTask.setTaskStatus(taskStatus);
        }

        taskService.addTaskToDatabase(newTask,subProjectId);
        return "redirect:/project/subproject/task/" + subProjectId;
    }


    //TODO Rigtig path skal sættes
    @PostMapping("/{subProjectId}/delete")
    public String deleteTask(@PathVariable int subProjectId,
                             @PathVariable int taskId) {

        taskService.deleteTask(taskService.getTaskById(taskId));

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

        Task task = taskService.getTaskById(taskId);

        if (task == null) {
            return ("redirect:/employee/" + employeeId);
        }

        model.addAttribute("task", task);
        return "editTask";
    }


}
