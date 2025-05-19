package org.example.alphasolutions.model;

import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public class Task {

    private int taskId;
    private int subProjectId;
    private String taskName;
    private String taskDescription;
    private LocalDate taskStartDate;
    private LocalDate taskEndDate;
    private TaskStatus taskStatus;
    private int taskEstimatedHours;
    private List<Employee> taskAssignedEmployees;


    public Task() {

    }

    public Task(int taskId, int subProjectId, String taskName, String taskDescription, LocalDate taskStartDate, LocalDate taskEndDate, TaskStatus taskStatus, int taskEstimatedHours, List<Employee> taskAssignedEmployees) {
        this.taskId = taskId;
        this.subProjectId = subProjectId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskStartDate = taskStartDate;
        this.taskEndDate = taskEndDate;
        this.taskStatus = taskStatus;
        this.taskEstimatedHours = taskEstimatedHours;
        this.taskAssignedEmployees = taskAssignedEmployees;
    }


    //Getter & Setter

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(int subProjectId) {
        this.subProjectId = subProjectId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public LocalDate getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(LocalDate taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public LocalDate getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(LocalDate taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getTaskEstimatedHours() {
        return taskEstimatedHours;
    }

    public void setTaskEstimatedHours(int taskEstimatedHours) {
        this.taskEstimatedHours = taskEstimatedHours;
    }

    public List<Employee> getTaskAssignedEmployees() {
        return taskAssignedEmployees;
    }

    public void setTaskAssignedEmployees(List<Employee> taskAssignedEmployees) {
        this.taskAssignedEmployees = taskAssignedEmployees;
    }
}