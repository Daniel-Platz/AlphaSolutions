package org.example.alphasolutions.repository;

import org.example.alphasolutions.model.Task;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
                "  VALUES (?,?,?,?,?,?,?)";

        jdbcTemplate.update(sql,
                newTaskToAdd.getTaskName(),
                newTaskToAdd.getTaskDescription(),
                newTaskToAdd.getTaskStartDate(),
                newTaskToAdd.getTaskEndDate(),
                newTaskToAdd.getTaskEstimatedHours(),
                newTaskToAdd.getTaskStatus().toString(),
                subProjectId);
    }


    public void deleteTask(Task taskToDelete) {
        String sql = "DELETE FROM task WHERE task_id = ?";


        jdbcTemplate.update(sql, taskToDelete.getTaskId());
    }

    public Map<Integer, Task> getTasksBySubProjectId(int subProjectId) {
        String sql = "SELECT task_id, sub_project_id, " +
                " task_name AS taskName," +
                " task_description AS taskDescription," +
                " task_start_date AS taskStartDate," +
                " task_end_date AS taskEndDate," +
                " task_estimated_hours AS taskEstimatedHours," +
                " task_status AS taskStatus" +
                " FROM task WHERE sub_project_id = ?";

        List<Task> taskList = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Task.class), subProjectId);

        return taskList.stream().collect(Collectors.toMap(Task::getTaskId,task -> task));


    }


    public Task getTaskById(int taskId){
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

}
