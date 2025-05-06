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
                "JOIN project_employee ON Project.project_id = project_employee.project_id " +
                "WHERE project_employee.employee_id = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Project.class), employeeId);
    }

    public void addProjectToDB(Project newProjectToAdd) {
        String sql = "INSERT INTO project (project_name, project_description, project_start_date, project_end_date, project_estimated_hours, project_status)" +
                " VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                newProjectToAdd.getProjectName(),
                newProjectToAdd.getProjectDescription(),
                newProjectToAdd.getProjectStartDate(),
                newProjectToAdd.getProjectEndDate(),
                newProjectToAdd.getProjectEstimatedHours(),
                newProjectToAdd.getProjectStatus().name());
    }
}