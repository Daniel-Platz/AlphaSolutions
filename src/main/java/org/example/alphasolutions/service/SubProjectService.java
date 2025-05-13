package org.example.alphasolutions.service;

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

    public int addNewSubProject (SubProject subProject) {
        return subProjectRepository.addNewSubProject(subProject);
    }

    public void editSubProject (SubProject subProjectToEdit){
        subProjectRepository.editSubProject(subProjectToEdit);
    }

}
