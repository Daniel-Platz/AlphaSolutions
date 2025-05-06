package org.example.alphasolutions.service;

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

}
