package org.example.alphasolutions.model;

import org.example.alphasolutions.enums.ProjectStatus;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Project {

    private int projectId;
    private String projectName;
    private String projectDescription;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private int projectEstimatedHours;
    private ProjectStatus projectStatus;
    private List<SubProject> subProjects;
    private List<Employee> projectAssignedEmployees;

    public Project(int projectId, String projectName, String projectDescription, LocalDate projectStartDate, LocalDate projectEndDate, int projectEstimatedHours, ProjectStatus projectStatus, List<SubProject> subProjects, List<Employee> projectAssignedEmployees) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDescription = projectDescription;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.projectEstimatedHours = projectEstimatedHours;
        this.projectStatus = projectStatus;
        this.subProjects = subProjects;
        this.projectAssignedEmployees = projectAssignedEmployees;
    }

    public Project() {
    }


    //Getter & Setter

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public LocalDate getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(LocalDate projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public LocalDate getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(LocalDate projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public int getProjectEstimatedHours() {
        return projectEstimatedHours;
    }

    public void setProjectEstimatedHours(int projectEstimatedHours) {
        this.projectEstimatedHours = projectEstimatedHours;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public List<SubProject> getSubProjects() {
        return subProjects;
    }

    public void setSubProjects(List<SubProject> subProjects) {
        this.subProjects = subProjects;
    }

    public List<Employee> getProjectAssignedEmployees() {
        return projectAssignedEmployees;
    }

    public void setProjectAssignedEmployees(List<Employee> projectAssignedEmployees) {
        this.projectAssignedEmployees = projectAssignedEmployees;
    }


    //Methods
    //TODO
    public void addSubProject(SubProject subProject) {

    }

    //TODO
    public void removeSubProject(int subProjectId) {

    }

    //TODO
    public void getSubProject(int subProjectId) {

    }

    //TODO
    public void calculateProjectTotalHours() {

    }

    //TODO
    public void calculateProjectProgress() {

    }

    //TODO
    public void getProjectDeadline() {

    }

    //TODO
    public void updateProjectStatus(ProjectStatus projectStatus) {

    }

    //TODO
    public void getAllSubProjects() {

    }

    //TODO
    public void addEmployeeToProject(int employeeId){

    }

    //TODO
    public void removeEmployeeFromProject(int employeeId){

    }

    //TODO
    public void editProject(){

    }
}