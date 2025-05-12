package org.example.alphasolutions.model;

import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.enums.TaskStatus;

import java.util.Date;
import java.util.List;

public class Task {

    private int taskId;
    private String taskName;
    private String taskDescription;
    private Date taskStartDate;
    private Date taskEndDate;
    private TaskStatus taskStatus;
    private int taskEstimatedHours;
    private List<Employee> taskAssignedEmployees;

    public Task() {

    }

    public Task(int taskId, String taskName, String taskDescription, Date taskStartDate, Date taskEndDate, TaskStatus taskStatus, int taskEstimatedHours, List<Employee> taskAssignedEmployees) {
        this.taskId = taskId;
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

    public Date getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Date taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Date getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(Date taskEndDate) {
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

    // Methods
    public void calculateTaskTotalHours() {
        // TODO
    }

    public void calculateTaskProgress() {
        // TODO: udregn progress
    }

    public void getTaskDeadline() {
        // TODO
    }

    public void assignEmployeeToTask(int employeeId) {
        // TODO: find employee i ID og tilføj til taskAssignedEmployees
    }

    public void removeEmployeeFromTask(int employeeId) {
        // TODO: find employee af ID og fjerne fra taskAssignedEmployees
    }

    public void updateTaskStatus() {
        // TODO
    }

    public void editTask() {
        // TODO
    }


}