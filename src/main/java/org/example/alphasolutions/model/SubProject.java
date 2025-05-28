package org.example.alphasolutions.model;

import org.example.alphasolutions.enums.ProjectStatus;

import java.time.LocalDate;
import java.util.List;

public class SubProject {

    private int subProjectId;
    private int projectId;
    private String subProjectName;
    private String subProjectDescription;
    private LocalDate subProjectStartDate;
    private LocalDate subProjectEndDate;
    private int subProjectEstimatedHours;
    private ProjectStatus subProjectStatus;
    private List<Task> tasks;
    private int subProjectActualHours;

    public SubProject(int subProjectId, int projectId, String subProjectName, String subProjectDescription, LocalDate subProjectStartDate, LocalDate subProjectEndDate, int subProjectEstimatedHours, ProjectStatus subProjectStatus, List<Task> tasks) {
        this.subProjectId = subProjectId;
        this.projectId = projectId;
        this.subProjectName = subProjectName;
        this.subProjectDescription = subProjectDescription;
        this.subProjectStartDate = subProjectStartDate;
        this.subProjectEndDate = subProjectEndDate;
        this.subProjectEstimatedHours = subProjectEstimatedHours;
        this.subProjectStatus = subProjectStatus;
        this.tasks = tasks;
    }

    public SubProject() {
    }


    //Getter & Setter

    public int getSubProjectId() {
        return subProjectId;
    }

    public void setSubProjectId(int subProjectId) {
        this.subProjectId = subProjectId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getSubProjectName() {
        return subProjectName;
    }

    public void setSubProjectName(String subProjectName) {
        this.subProjectName = subProjectName;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public ProjectStatus getSubProjectStatus() {
        return subProjectStatus;
    }

    public void setSubProjectStatus(ProjectStatus subProjectStatus) {
        this.subProjectStatus = subProjectStatus;
    }

    public int getSubProjectEstimatedHours() {
        return subProjectEstimatedHours;
    }

    public void setSubProjectEstimatedHours(int subProjectEstimatedHours) {
        this.subProjectEstimatedHours = subProjectEstimatedHours;
    }

    public LocalDate getSubProjectEndDate() {
        return subProjectEndDate;
    }

    public void setSubProjectEndDate(LocalDate subProjectEndDate) {
        this.subProjectEndDate = subProjectEndDate;
    }

    public LocalDate getSubProjectStartDate() {
        return subProjectStartDate;
    }

    public void setSubProjectStartDate(LocalDate subProjectStartDate) {
        this.subProjectStartDate = subProjectStartDate;
    }

    public String getSubProjectDescription() {
        return subProjectDescription;
    }

    public void setSubProjectDescription(String subProjectDescription) {
        this.subProjectDescription = subProjectDescription;
    }

    public void setSubProjectActualHours(int subProjectActualHours) {
        this.subProjectActualHours = subProjectActualHours;
    }

    public int getSubProjectActualHours() {
        return subProjectActualHours;
    }
}