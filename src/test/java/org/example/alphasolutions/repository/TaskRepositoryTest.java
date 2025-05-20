package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.enums.TaskStatus;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:h2init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void testAddNewTask() {
        Task taskToAdd = new Task();
        taskToAdd.setSubProjectId(1);
        taskToAdd.setTaskName("Test Task");
        taskToAdd.setTaskDescription("This is a test description");
        taskToAdd.setTaskStartDate(LocalDate.now());
        taskToAdd.setTaskEndDate(LocalDate.now().plusDays(10));
        taskToAdd.setTaskEstimatedHours(10);
        taskToAdd.setTaskStatus(TaskStatus.IN_PROGRESS);

        int newTaskId = taskRepository.addNewTask(taskToAdd);

        assertTrue(newTaskId > 0, "Should return a valid ID");

        List<Task> tasks = taskRepository.findAllTasks();

        Task createdTask = null;
        for (Task t : tasks) {
            if ("Test Task".equals(t.getTaskName())) {
                createdTask = t;
                break;
            }
        }
        assertNotNull(createdTask);
        assertEquals("This is a test description", createdTask.getTaskDescription(), "Description should match");
        assertEquals(10, createdTask.getTaskEstimatedHours(),
                "Estimated hours should match");
        assertEquals(TaskStatus.IN_PROGRESS, createdTask.getTaskStatus(),
                "Status should match");
    }

    //TODO, virker ikke
    @Test
    void testDeleteTask() {
        int taskId = 1;
        int subProjectId = 1;

        List<Task> tasksBeforeDelete = taskRepository.findTasksBySubProjectId(subProjectId);
        assertEquals(3,tasksBeforeDelete.size());

        taskRepository.deleteTask(taskId);

        List<Task> tasksAfterDelete = taskRepository.findTasksBySubProjectId(subProjectId);
        assertEquals(2,tasksAfterDelete.size());
    }

    @Test
    void testFindTaskBySubProjectId() {
        int subProjectId = 1;

        List<Task> tasks = taskRepository.findTasksBySubProjectId(subProjectId);

        assertEquals(3, tasks.size());
    }


    @Test
    void testFindTaskByTaskId() {

        //Arrange
        int taskId = 1;

        //Act
        Task actualTask = taskRepository.findTaskByTaskId(taskId);

        //Assert
        assertEquals(taskId, actualTask.getTaskId());

    }

    //TODO testEditTask
    @Test
    void testEditTask() {
        Task createdTask = new Task();
        createdTask.setTaskId(1);
        createdTask.setSubProjectId(1);
        createdTask.setTaskName("test Task");
        createdTask.setTaskDescription("This is a test description");
        createdTask.setTaskStartDate(LocalDate.now());
        createdTask.setTaskEndDate(LocalDate.now().plusDays(10));
        createdTask.setTaskEstimatedHours(25);
        createdTask.setTaskStatus(TaskStatus.IN_PROGRESS);

        int taskId = taskRepository.addNewTask(createdTask);

        Task taskToEdit = new Task();
        taskToEdit.setTaskId(taskId);
        taskToEdit.setSubProjectId(1);
        taskToEdit.setTaskName("Edited Task");
        taskToEdit.setTaskDescription("Edited description");
        taskToEdit.setTaskStartDate(LocalDate.now().plusDays(1));
        taskToEdit.setTaskEndDate(LocalDate.now().plusDays(15));
        taskToEdit.setTaskEstimatedHours(50);
        taskToEdit.setTaskStatus(TaskStatus.DELAYED);

        taskRepository.editTask(taskToEdit);

        Task editedTask = taskRepository.findTaskByTaskId(taskId);
        assertNotNull(editedTask);
        assertEquals("Edited Task", editedTask.getTaskName());
        assertEquals("Edited description", editedTask.getTaskDescription());
        assertEquals(LocalDate.now().plusDays(1), editedTask.getTaskStartDate());
        assertEquals(LocalDate.now().plusDays(15), editedTask.getTaskEndDate());
        assertEquals(50, editedTask.getTaskEstimatedHours());
        assertEquals(TaskStatus.DELAYED, editedTask.getTaskStatus());

    }


}
