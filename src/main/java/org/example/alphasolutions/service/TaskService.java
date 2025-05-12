package org.example.alphasolutions.service;

import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void addTaskToDatabase(Task newTask, int subProjectId) {
        taskRepository.addTask(newTask, subProjectId);
    }

    public void deleteTask(Task taskToDelete){
        taskRepository.deleteTask(taskToDelete);
    }

    public Task getTaskById(int taskId) {
        return taskRepository.getTaskById(taskId);
    }

    public boolean editTask(Task newTask) {
        return taskRepository.editTask(newTask);
    }

}
