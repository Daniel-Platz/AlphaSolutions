package org.example.alphasolutions.repository;


import org.example.alphasolutions.model.Task;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



import java.util.List;


@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void addTask(Task newTaskToAdd, int subProjectId) {
        String sql = "INSERT INTO task(task_name, task_description, task_start_date, " +
                "task_end_date, task_estimated_hours, task_status, sub_project_id)" +
                " VALUES (?,?,?,?,?,?,?)";

        // Anvend et KeyHolder til at få det auto-genererede ID
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newTaskToAdd.getTaskName());
            ps.setString(2, newTaskToAdd.getTaskDescription());
            ps.setDate(3, new java.sql.Date(newTaskToAdd.getTaskStartDate().getTime()));
            ps.setDate(4, new java.sql.Date(newTaskToAdd.getTaskEndDate().getTime()));
            ps.setDouble(5, newTaskToAdd.getTaskEstimatedHours());
            ps.setString(6, newTaskToAdd.getTaskStatus().toString());
            ps.setInt(7, subProjectId);
            return ps;
        }, keyHolder);

        // Sæt det genererede ID på task-objektet
        Number key = keyHolder.getKey();
        if (key != null) {
            newTaskToAdd.setTaskId(key.intValue());
        }
    }



//    @Transactional
//    public void addTask(Task newTaskToAdd, int subProjectId) {
//
//        String sql = "INSERT INTO task(task_name, task_description, task_start_date, " +
//                "task_end_date, task_estimated_hours, task_status, sub_project_id)" +
//                "  VALUES (?,?,?,?,?,?,?)";
//
//        jdbcTemplate.update(sql,
//                newTaskToAdd.getTaskName(),
//                newTaskToAdd.getTaskDescription(),
//                newTaskToAdd.getTaskStartDate(),
//                newTaskToAdd.getTaskEndDate(),
//                newTaskToAdd.getTaskEstimatedHours(),
//                newTaskToAdd.getTaskStatus().toString(),
//                subProjectId);
//    }


    public void deleteTask(Task taskToDelete) {
        String sql = "DELETE FROM task WHERE task_id = ?";


        jdbcTemplate.update(sql, taskToDelete.getTaskId());
    }

    public List<Task> getTasksBySubProjectId(int subProjectId) {
        String sql = "SELECT * FROM task WHERE sub_project_id = ?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Task.class), subProjectId);
    }



    public Task getTaskByTaskId(int taskId){
        String sql = "SELECT task_id, sub_project_id," +
                "task_name AS taskName, " +
                "task_description AS taskDescription, " +
                "task_start_date AS taskStartDate, " +
                "task_end_date AS taskEndDate, " +
                "task_estimated_hours AS taskEstimatedHours, " +
                "task_status AS taskStatus " +
                "FROM task WHERE task_id = ?";

        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Task.class),taskId);
    }

    @Transactional
    //TODO skal det være muligt at ændre sub-projectID her?
    public boolean editTask(Task newTask) {
        String sql = "UPDATE Task " +
                "SET task_name = ?, " +
                "task_description = ?, " +
                "task_start_date = ?, " +
                "task_end_date = ?, " +
                "task_estimated_hours = ?, " +
                "task_status = ? " +
                "WHERE task_id = ?";

        try {
            return jdbcTemplate.update(sql, newTask.getTaskName(),
                    newTask.getTaskDescription(),
                    newTask.getTaskStartDate(),
                    newTask.getTaskEndDate(),
                    newTask.getTaskEstimatedHours(),
                    newTask.getTaskStatus(),
                    newTask.getTaskId()) == 1;
        } catch (DataAccessException e) {
            return false;
        }
    }

    public List<Task> getAlltasks() {
        String sql = "SELECT * FROM task";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Task.class));
    }


}
