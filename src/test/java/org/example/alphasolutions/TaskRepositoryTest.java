package org.example.alphasolutions;

import net.bytebuddy.dynamic.TypeResolutionStrategy;
import org.example.alphasolutions.enums.TaskStatus;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:h2init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;
    //private Task testTask;

    @Test
    public void addTask() throws SQLException {

        //Arrange
        int subProjectId = 1;

        //Opret en ny task
        Task newTask = new Task();
        newTask.setTaskName("Ny opgave");
        newTask.setTaskDescription("Ny beskrivelse");
        newTask.setTaskStartDate(new Date(2025,06,05));
        newTask.setTaskEndDate(new Date(2025,07,12));
        newTask.setTaskEstimatedHours(10);
        newTask.setTaskStatus(TaskStatus.IN_PROGRESS);

        //Act
        taskRepository.addTask(newTask, subProjectId);

        //Assert
        Map<Integer, Task> tasks = taskRepository.getTasksBySubProjectId(subProjectId);
        assertNotNull(tasks, "Kunne ikke hente opgaver efter tilføjelse");
        assertFalse(tasks.isEmpty(), "Ingen opgaver blev fundet efter tilføjelse");

        boolean found = false;
        for (Task task : tasks.values()) {
            if (task.getTaskId() != 1 ) {
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("WARNING: Kunne ikke finde opgave med taskId '1'");
        }

        assertTrue(found, "Den tilføjede opgave blev ikke fundet");
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
        Task actualTask = taskRepository.getTaskById(taskId);

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
