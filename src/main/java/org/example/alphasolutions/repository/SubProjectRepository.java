package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SubProjectRepository {
    private final JdbcTemplate jdbcTemplate;

    public SubProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addTask(Task newTask) {
    }

    public void removeTask(int taskId) {

    }

    public void getTask(int taskId) {

    }

    public void calculateSubProjectTotalHours() {

    }

    public void calculateSubProjectProgress() {

    }

    public void getSubProjectDeadline() {

    }

    public void updateSubProjectStatus(ProjectStatus subProjectStatus) {

    }

    public void editSubProject() {

    }
}
