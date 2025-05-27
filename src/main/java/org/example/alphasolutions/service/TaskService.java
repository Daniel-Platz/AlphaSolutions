package org.example.alphasolutions.service;

import org.example.alphasolutions.exception.InsufficientHoursException;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public int addNewTask(Task task, int subProjectEstimatedHours) {
        validateTaskHours(task, subProjectEstimatedHours);
        return taskRepository.addNewTask(task);
    }

    public void deleteTask(int taskId){
        taskRepository.deleteTask(taskId);
    }

    public List<Task> findTaskBySubProjectId(int subProjectId){
        return taskRepository.findTasksBySubProjectId(subProjectId);
    }

    public Task findTaskByTaskId(int taskId) {
        return taskRepository.findTaskByTaskId(taskId);
    }

    public void editTask (Task taskToEdit, int subProjectEstimatedHours){
        validateTaskHours(taskToEdit, subProjectEstimatedHours);
        taskRepository.editTask(taskToEdit);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAllTasks();
    }

    public void assignEmployeeToTask(int employeeId, int taskId){
        taskRepository.assignEmployeeToTask(employeeId, taskId);
    }

    public List<Employee> findAssignedEmployeesByTaskId(int taskId) {
        return taskRepository.findAssignedEmployeesByTaskId(taskId);
    }

    public void removeEmployeeFromTask(int employeeId, int taskId){
        taskRepository.removeEmployeeFromTask(employeeId, taskId);
    }

    public void registerHours(int taskId, int hoursToAdd) {
        taskRepository.registerHours(taskId, hoursToAdd);
    }

    private void validateTaskHours(Task task, int subProjectEstimatedHours) {
        int totalTaskHours = taskRepository.calculateTotalTaskEstimatedHours(task.getSubProjectId());

        if (task.getTaskId() > 0) {
            Task existingTask = taskRepository.findTaskByTaskId(task.getTaskId());
            totalTaskHours -= existingTask.getTaskEstimatedHours();
        }

        int newTotal = totalTaskHours + task.getTaskEstimatedHours();

        if (newTotal > subProjectEstimatedHours) {
            throw new InsufficientHoursException();
        }
    }

    public boolean isEmployeeAssignedToTask (int employeeId, int taskId){
        return taskRepository.isEmployeeAssignedToTask(employeeId,taskId);
    }
}
