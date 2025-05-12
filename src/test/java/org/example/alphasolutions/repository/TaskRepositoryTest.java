package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.TaskStatus;
import org.example.alphasolutions.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:h2init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void addTask() throws SQLException {
        //Arrange
        int subProjectId = 1;

        //Opret en ny task
        Task newTask = new Task();
        newTask.setTaskName("Ny opgave");
        newTask.setTaskDescription("Ny beskrivelse");

        // Brug java.util.Calendar i stedet for direkte Date konstruktion
        Calendar startCal = Calendar.getInstance();
        startCal.set(2025, Calendar.JUNE, 5); // Bemærk: måneder er 0-baseret i Calendar
        newTask.setTaskStartDate(startCal.getTime());

        Calendar endCal = Calendar.getInstance();
        endCal.set(2025, Calendar.JULY, 12);
        newTask.setTaskEndDate(endCal.getTime());

        newTask.setTaskEstimatedHours(10);
        newTask.setTaskStatus(TaskStatus.IN_PROGRESS);

        //Act
        taskRepository.addTask(newTask, subProjectId);

        // Efter addTask bør newTask have fået et ID tildelt
        assertNotNull(newTask.getTaskId(), "Task ID blev ikke sat efter tilføjelse");

        //Assert
        Map<Integer, Task> tasks = taskRepository.getTasksBySubProjectId(subProjectId);
        assertNotNull(tasks, "Kunne ikke hente opgaver efter tilføjelse");
        assertFalse(tasks.isEmpty(), "Ingen opgaver blev fundet efter tilføjelse");

        // Verificer at den nye task blev tilføjet - nu hvor ID'et er kendt
        assertTrue(tasks.containsKey(newTask.getTaskId()),
                "Den tilføjede opgave med ID " + newTask.getTaskId() + " blev ikke fundet");

        // Verificer opgavens indhold
        Task retrievedTask = tasks.get(newTask.getTaskId());
        assertEquals("Ny opgave", retrievedTask.getTaskName());
        assertEquals("Ny beskrivelse", retrievedTask.getTaskDescription());
        assertEquals(10, retrievedTask.getTaskEstimatedHours());
        assertEquals(TaskStatus.IN_PROGRESS, retrievedTask.getTaskStatus());
    }

    @Test
    //TODO Skal resten af task-felterne (beskrivelse etc.) med i testen, ligesom i WISHLIST PROJECT?
    public void getTasksBySubProjectId() throws SQLException {

        //Arrange
        int subProjectId = 1;

        //Act
        Map<Integer, Task> tasks = taskRepository.getTasksBySubProjectId(subProjectId);

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
        Task actualTask = taskRepository.getTaskByTaskId(taskId);

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













}
