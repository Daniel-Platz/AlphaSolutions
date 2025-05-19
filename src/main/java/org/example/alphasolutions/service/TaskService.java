package org.example.alphasolutions.service;

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

    public int addNewTask(Task task) {
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

    public void editTask (Task taskToEdit){
        taskRepository.editTask(taskToEdit);
    }

    public List<Task> findAllTasks() {
        return taskRepository.findAllTasks();
    }

    public void assignEmployeeToTask(int employeeId, int taskId){
        taskRepository.assignEmployeeToTask(employeeId, taskId);
    }

}
