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

    public int calculateSubProjectTotalHours(int subProjectId) {
        String sql = "SELECT SUM(TASK_ESTIMATED_HOURS) FROM TASK WHERE SUB_PROJECT_ID = ?";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, subProjectId);
        return result != null ? result : 0;
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
