package org.example.alphasolutions.service;

import org.example.alphasolutions.exception.InsufficientHoursException;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.repository.SubProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProjectService {

    private final SubProjectRepository subProjectRepository;

    public SubProjectService(SubProjectRepository subProjectRepository) {
        this.subProjectRepository = subProjectRepository;
    }

    public List<Task> findTasksBySubProjectId (int subProjectId) {
        return subProjectRepository.findTasksBySubProjectId(subProjectId);
    }

    public SubProject findSubProjectById(int subProjectId){
        return subProjectRepository.findSubProjectById(subProjectId);
    }

    public int calculateSubProjectTotalHours (int subProjectId) {
        return subProjectRepository.calculateSubProjectTotalHours(subProjectId);
    }

    public int addNewSubProject (SubProject subProject, int projectEstimatedHours) {
        validateSubProjectHours(subProject, projectEstimatedHours);
        return subProjectRepository.addNewSubProject(subProject);
    }

    public void deleteSubProject(int subProjectId) {
        subProjectRepository.deleteSubProject(subProjectId);
    }

    public void editSubProject (SubProject subProjectToEdit, int projectEstimatedHours){
        validateSubProjectHours(subProjectToEdit, projectEstimatedHours);
        subProjectRepository.editSubProject(subProjectToEdit);
    }

    private void validateSubProjectHours(SubProject subProject, int projectEstimatedHours) {
        int totalSubProjectHours = subProjectRepository.getTotalSubProjectEstimatedHours(subProject.getProjectId());

        if (subProject.getSubProjectId() > 0) {
            SubProject existingSubProject = subProjectRepository.findSubProjectById(subProject.getSubProjectId());
            totalSubProjectHours -= existingSubProject.getSubProjectEstimatedHours();
        }

        int newTotal = totalSubProjectHours + subProject.getSubProjectEstimatedHours();

        if (newTotal > projectEstimatedHours) {
            throw new InsufficientHoursException();
        }
    }
}

