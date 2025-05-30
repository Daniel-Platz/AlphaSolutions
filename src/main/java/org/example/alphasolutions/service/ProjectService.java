package org.example.alphasolutions.service;

import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final SubProjectService subProjectService;

    public ProjectService(ProjectRepository projectRepository, SubProjectService subProjectService) {
        this.projectRepository = projectRepository;
        this.subProjectService = subProjectService;
    }

    public List<Project> findAllProjects(ProjectStatus projectStatus) {
        return projectRepository.findAllProjects(projectStatus);
    }

    public List<Project> findProjectsByEmployeeId(int employeeId) {
        return projectRepository.findProjectsByEmployeeId(employeeId);
    }

    public Project findProjectById (int projectId) {
        return projectRepository.findProjectById(projectId);
    }

    public List<SubProject> findSubProjectsByProjectId(int projectId) {
        return projectRepository.findSubProjectsByProjectId(projectId);
    }

    public List<Employee> findAssignedEmployeesByProjectId(int projectId) {
        return projectRepository.findAssignedEmployeesByProjectId(projectId);
    }

    public int addProjectToDB(Project newProjectToAdd){
        return projectRepository.addProjectToDB(newProjectToAdd);
    }

    public void assignEmployeeToProject(int employeeId, int projectId){
        projectRepository.assignEmployeeToProject(employeeId, projectId);
    }

    public void deleteProjectFromDB(int projectId){
        projectRepository.deleteProjectFromDB(projectId);
    }

    public void updateProject(Project projectToEdit, int oldManagerId){
        projectRepository.updateProject(projectToEdit, oldManagerId);
    }

    public void removeEmployeeFromProject(int employeeId, int projectId) {
        projectRepository.removeEmployeeFromProject(employeeId, projectId);
    }

    public int calculateProjectActualHours(int projectId) {
        List<SubProject> subProjects = findSubProjectsByProjectId(projectId);
        int totalActualHours = 0;
        for (SubProject subProject : subProjects) {
            totalActualHours += subProjectService.calculateActualHours(subProject.getSubProjectId());
        }
        return totalActualHours;
    }

    public int calculateSubProjectActualHours (int subProjectId){
        return subProjectService.calculateActualHours(subProjectId);
    }
}