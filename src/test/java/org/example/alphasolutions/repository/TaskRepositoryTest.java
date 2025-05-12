package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.TaskStatus;
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
/*
    @Test
    //TODO Skal resten af task-felterne (beskrivelse etc.) med i testen, ligesom i WISHLIST PROJECT?
    public void getTasksBySubProjectId() throws SQLException {

        //Arrange
        int subProjectId = 1;

        //Act
        List<Task> tasks = taskRepository.getTasksBySubProjectId(subProjectId);

        //Assert
        assertNotNull(tasks, "getTasksBySubProjectId retunerede null");
        assertNotNull(tasks.isEmpty(), "Ingen opgaver blev fundet");

        //Verificerer at alle ønsker i map'et har korrekte værdier
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            Integer taskId = entry.getKey();
            Task task = entry.getValue();

            assertNotNull(taskId, "Opgave ID er null");
            assertNotNull(task, "Opgave objekt er null");
            assertEquals(taskId, task.getTaskId(), "Opgave ID er map-nøgle og opgave objekt stemmer ikke overens");

            //Tjek at nødvendige felter ikke er null eller tomme
            assertNotNull(task.getTaskName(), "Opgave titel er null");
        }
    }

    @Test
    //TODO Er denne test korrekt?? Virker alt for simpel
    public void getTaskById() throws SQLException {

        //Arrange
        int taskId = 1;

        //Act
        Task actualTask = taskRepository.findTaskByTaskId(taskId);

        //Assert
        assertEquals(taskId, actualTask.getTaskId());


    }

    @Test
    public void deleteTask() throws SQLException {

        //Arrange
        int subProjectId = 1;

        //Henter alle tasks fra sub-projektet
        Map<Integer, Task> tasks = taskRepository.getTasksBySubProjectId(subProjectId);
        assertFalse(tasks.isEmpty(), "Ingen opgaver fundet til test af sletning");

        //Vælg den første opgave til at slette
        Integer taskIdToDelete = tasks.keySet().iterator().next();
        Task taskToDelete = tasks.get(taskIdToDelete);
        assertNotNull(taskToDelete, "Kunne ikke finde en opgave at seltte");

        //Act
        taskRepository.deleteTask(taskToDelete);

        //Assert
        Map<Integer, Task> updatedTasks = taskRepository.getTasksBySubProjectId(subProjectId);
        assertFalse(updatedTasks.containsKey(taskIdToDelete), "Opgaven blev ikke slettet fra sub-projektet");
    }









 */





}
