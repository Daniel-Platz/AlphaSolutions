package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.exception.CreationException;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ProjectRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Project> findAllProjects(ProjectStatus projectStatus) {
        if (projectStatus != null) // It's checking for null so it might be reused later for getting other statuses if needed
        {
            String sql = "SELECT * FROM Project WHERE project_status = ?";
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Project.class), projectStatus.name());
        }
        else { // If status is null it will get all projects that don't have the Archived status.
            String sql = "SELECT * FROM Project WHERE NOT project_status = ?";
            return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Project.class), ProjectStatus.ARCHIVED.name());
        }
    }

    public List<Project> findProjectsByEmployeeId(Integer employeeId) {
        String sql = "SELECT Project.* FROM Project " +
                "JOIN project_employee ON Project.project_id = project_employee.project_id " +
                "WHERE project_employee.employee_id = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Project.class), employeeId);
    }

    public Project findProjectById(int projectId) {
        String sql = "SELECT * FROM Project WHERE project_id = ?";
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Project.class), projectId);
    }

    public List<SubProject> findSubProjectsByProjectId(int projectId) {
        String sql = "SELECT * FROM sub_project WHERE project_id = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(SubProject.class), projectId);
    }

    public List<Employee> findAssignedEmployeesByProjectId(int projectId) {
        String sql = "SELECT employee.* FROM employee " +
                "JOIN project_employee ON employee.employee_id = project_employee.employee_id " +
                "WHERE project_employee.project_id = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Employee.class), projectId);
    }

    @Transactional
    public int addProjectToDB(Project newProjectToAdd) {
        String sql = "INSERT INTO project (project_name, project_description, project_start_date, project_end_date, " +
                "project_estimated_hours, project_status, manager_id)" +
                " VALUES (?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newProjectToAdd.getProjectName());
            ps.setString(2, newProjectToAdd.getProjectDescription());
            ps.setDate(3, Date.valueOf(newProjectToAdd.getProjectStartDate()));
            ps.setDate(4, Date.valueOf(newProjectToAdd.getProjectEndDate()));
            ps.setInt(5, newProjectToAdd.getProjectEstimatedHours());
            ps.setString(6, newProjectToAdd.getProjectStatus().name());
            ps.setInt(7, newProjectToAdd.getManagerId());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key==null) {
            throw new CreationException();
        }

        return key.intValue();
    }

    @Transactional
    public void assignEmployeeToProject(int employeeId, int projectId){
        String sql = "INSERT INTO project_employee (employee_id, project_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, employeeId, projectId);
    }

    @Transactional
    public void deleteProjectFromDB(int projectId){
        String sql = "DELETE FROM project WHERE project_id = ?";
        jdbcTemplate.update(sql, projectId);
    }

    @Transactional
    public void updateProject(Project projectToEdit, int oldManagerId){
        String sql = "UPDATE project SET " +
                "project_name = ?, " +
                "project_description = ?, " +
                "project_start_date = ?, " +
                "project_end_date = ?, " +
                "project_estimated_hours = ?, " +
                "project_status = ?, " +
                "manager_id = ? " +
                "WHERE project_id = ?";

        jdbcTemplate.update(sql, projectToEdit.getProjectName(), projectToEdit.getProjectDescription(), projectToEdit.getProjectStartDate(),
                projectToEdit.getProjectEndDate(), projectToEdit.getProjectEstimatedHours(), projectToEdit.getProjectStatus().name(),
                projectToEdit.getManagerId(), projectToEdit.getProjectId());

        if (oldManagerId != projectToEdit.getManagerId()){
            replaceOldManager(oldManagerId, projectToEdit.getManagerId(), projectToEdit.getProjectId());
        }
    }

    @Transactional
    protected void replaceOldManager(int oldManagerId, int newManagerId, int projectId){
        String sql = "DELETE FROM project_employee WHERE employee_id = ? AND project_id = ?";
        jdbcTemplate.update(sql, oldManagerId, projectId);
        sql = "INSERT INTO project_employee (employee_id, project_id) VALUES (?,?)";
        jdbcTemplate.update(sql, newManagerId, projectId);
    }
}