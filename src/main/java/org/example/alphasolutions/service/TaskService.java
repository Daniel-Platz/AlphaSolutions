package org.example.alphasolutions.service;

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

    public void addTaskToDatabase(Task newTask, int subProjectId) {
        taskRepository.addTask(newTask, subProjectId);
    }

    public void deleteTask(Task taskToDelete){
        taskRepository.deleteTask(taskToDelete);
    }

    public List<Task> getTaskBySubProjectId(int subProjectId){
        return taskRepository.getTasksBySubProjectId(subProjectId);
    }

    public Task getTaskByTaskId(int taskId) {
        return taskRepository.getTaskByTaskId(taskId);
    }

    public boolean editTask(Task newTask) {
        return taskRepository.editTask(newTask);
    }

    public List<Task> getAllTasks() {
        return taskRepository.getAlltasks();
    }

}
