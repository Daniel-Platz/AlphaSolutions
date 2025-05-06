package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.model.Task;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubProjectRepository {
    private final JdbcTemplate jdbcTemplate;

    public SubProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Task> findTasksBySubProjectId(int subProjectId) {
        String sql = "SELECT * FROM TASK WHERE SUB_PROJECT_ID = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Task.class), subProjectId);
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
