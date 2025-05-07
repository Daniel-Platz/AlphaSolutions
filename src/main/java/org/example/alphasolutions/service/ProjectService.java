package org.example.alphasolutions.service;

import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAllProjects();
    }

    public List<Project> findProjectsByEmployeeId(int employeeId) {
        return projectRepository.findProjectsByEmployeeId(employeeId);
    }

    public int addProjectToDB(Project newProjectToAdd){
        return projectRepository.addProjectToDB(newProjectToAdd);
    }

    public void assignEmployeeToProject(int employeeId, int projectId){
        projectRepository.assignEmployeeToProject(employeeId, projectId);
    }
}