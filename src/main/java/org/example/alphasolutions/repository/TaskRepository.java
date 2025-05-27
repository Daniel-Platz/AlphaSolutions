package org.example.alphasolutions.repository;

import org.example.alphasolutions.exception.CreationException;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.Task;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public int addNewTask(Task newTask) {
        String sql = "INSERT INTO task(sub_project_id, task_name, task_description, task_start_date, " +
                "task_end_date, task_estimated_hours, task_status)" +
                " VALUES (?,?,?,?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, newTask.getSubProjectId());
            ps.setString(2, newTask.getTaskName());
            ps.setString(3, newTask.getTaskDescription());
            ps.setDate(4, Date.valueOf(newTask.getTaskStartDate()));
            ps.setDate(5, Date.valueOf(newTask.getTaskEndDate()));
            ps.setDouble(6, newTask.getTaskEstimatedHours());
            ps.setString(7, newTask.getTaskStatus().toString());

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new CreationException();
        }

        return key.intValue();
    }

    public void deleteTask(int taskId) {
        String sql = "DELETE FROM task WHERE task_id = ?";
        jdbcTemplate.update(sql, taskId);
    }

    public List<Task> findTasksBySubProjectId(int subProjectId) {
        String sql = "SELECT * FROM task WHERE sub_project_id = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Task.class), subProjectId);
    }

    public Task findTaskByTaskId(int taskId) {
        String sql = "SELECT * FROM task WHERE task_id = ?";
        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Task.class), taskId);
    }


    public void editTask(Task taskToEdit) {
        String sql = "UPDATE task SET " +
                "task_name = ?, " +
                "task_description = ?, " +
                "task_start_date = ?, " +
                "task_end_date = ?, " +
                "task_estimated_hours = ?, " +
                "task_status = ? " +
                "WHERE task_id = ?";

        jdbcTemplate.update(sql,
                taskToEdit.getTaskName(),
                taskToEdit.getTaskDescription(),
                taskToEdit.getTaskStartDate(),
                taskToEdit.getTaskEndDate(),
                taskToEdit.getTaskEstimatedHours(),
                taskToEdit.getTaskStatus().name(),
                taskToEdit.getTaskId());
    }

    public List<Task> findAllTasks() {
        String sql = "SELECT * FROM task";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Task.class));
    }

    @Transactional
    public void assignEmployeeToTask(int employeeId, int taskId) {
        String sql = "INSERT INTO Employee_Task (employee_id, task_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, employeeId, taskId);
    }

    public List<Employee> findAssignedEmployeesByTaskId(int taskId) {
        String sql = "SELECT e.* FROM employee e " +
                "JOIN Employee_Task te ON e.employee_id = te.employee_id " +
                "WHERE te.task_id = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Employee.class), taskId);
    }

    @Transactional
    public void removeEmployeeFromTask(int employeeId, int taskId) {
        String sql = "DELETE FROM Employee_Task WHERE employee_id = ? AND task_id = ?";
        jdbcTemplate.update(sql, employeeId, taskId);
    }

    public void registerHours(int taskId, int hoursToAdd) {
        try {
            String selectSql = "SELECT task_actual_hours FROM task WHERE task_id = ?";
            Integer currentHours = jdbcTemplate.queryForObject(selectSql, Integer.class, taskId);

            int updatedHours = (currentHours != null ? currentHours : 0) + hoursToAdd;
            String updateSql = "UPDATE task SET task_actual_hours = ? WHERE task_id = ?";
            jdbcTemplate.update(updateSql, updatedHours, taskId);

        } catch (EmptyResultDataAccessException e) {
            String updateSql = "UPDATE task SET task_actual_hours = ? WHERE task_id = ?";
            jdbcTemplate.update(updateSql, hoursToAdd, taskId);
        }
    }

    public int calculateTotalTaskEstimatedHours(int subProjectId) {
        String sql = "SELECT IFNULL(SUM(task_estimated_hours), 0) FROM task WHERE sub_project_id = ?";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, subProjectId);
        return result != null ? result : 0;
    }

    public boolean isEmployeeAssignedToTask(int employeeId, int taskId){
        String sql = "SELECT 1 FROM employee_Task WHERE employee_id = ? AND task_id = ?";

        try {
            jdbcTemplate.queryForObject(sql, Integer.class, employeeId, taskId);
            return true;
        } catch (EmptyResultDataAccessException e){
            return false;
        }
    }
}
