package org.example.alphasolutions.repository;

import org.example.alphasolutions.model.Project;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Project> findAllProjects() {
        String sql = "SELECT * FROM Project";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Project.class));
    }

    public List<Project> findProjectsByEmployeeId(Integer employeeId) {
        String sql = "SELECT Project.* FROM Project " +
                "JOIN Project_Employee ON Project.project_id = Project_Employee.project_id " +
                "WHERE Project_Employee.employee_id = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Project.class), employeeId);
    }
}