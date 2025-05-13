package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.exception.CreationException;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class SubProjectRepository {
    private final JdbcTemplate jdbcTemplate;

    public SubProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SubProject> findAllSubProjects() {
        String sql = "SELECT * FROM sub_project";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SubProject.class));
    }

    public SubProject findSubProjectById(int subProjectId) {
        String sql = "SELECT * FROM sub_project WHERE sub_project_id = ?";
        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(SubProject.class), subProjectId);
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

    public int addNewSubProject(SubProject newSubProject) {
        String sql = "INSERT INTO sub_project (project_id, sub_project_name, sub_project_description, sub_project_start_date, sub_project_end_date, sub_project_estimated_hours, sub_project_status)" +
                " VALUES (?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, newSubProject.getProjectId());
            ps.setString(2, newSubProject.getSubProjectName());
            ps.setString(3, newSubProject.getSubProjectDescription());
            ps.setDate(4, Date.valueOf(newSubProject.getSubProjectStartDate()));
            ps.setDate(5, Date.valueOf(newSubProject.getSubProjectEndDate()));
            ps.setInt(6, newSubProject.getSubProjectEstimatedHours());
            ps.setString(7, newSubProject.getSubProjectStatus().name());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key==null) {
            throw new CreationException();
        }

        return key.intValue();
    }

    public void deleteSubProject(int subProjectId) {
        String sql = "DELETE FROM sub_project WHERE sub_project_id = ?";
        jdbcTemplate.update(sql, subProjectId);
    }

    public void editSubProject(SubProject subProjectToEdit) {
        String sql = "UPDATE sub_project SET " +
                "sub_project_name = ?, " +
                "sub_project_description = ?, " +
                "sub_project_start_date = ?, " +
                "sub_project_end_date = ?, " +
                "sub_project_estimated_hours = ?, " +
                "sub_project_status = ? " +
                "WHERE sub_project_id = ?";

        jdbcTemplate.update(sql,
                subProjectToEdit.getSubProjectName(),
                subProjectToEdit.getSubProjectDescription(),
                subProjectToEdit.getSubProjectStartDate(),
                subProjectToEdit.getSubProjectEndDate(),
                subProjectToEdit.getSubProjectEstimatedHours(),
                subProjectToEdit.getSubProjectStatus().name(),  // Convert enum to string
                subProjectToEdit.getSubProjectId());
    }

    public void calculateSubProjectProgress() {

    }

    public void getSubProjectDeadline() {

    }

    public void updateSubProjectStatus(ProjectStatus subProjectStatus) {

    }


}
