package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.TaskStatus;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
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
        // Arrange
        Task newTask = new Task();
        newTask.setSubProjectId(1);
        newTask.setTaskName("New Test Task");
        newTask.setTaskDescription("This is a test task description");
        newTask.setTaskStartDate(LocalDate.now());
        newTask.setTaskEndDate(LocalDate.now().plusDays(7));
        newTask.setTaskEstimatedHours(15);
        newTask.setTaskStatus(TaskStatus.NOT_STARTED);

        // Act
        int taskId = taskRepository.addNewTask(newTask);

        // Assert
        assertTrue(taskId > 0, "Task ID should be positive");

        Task retrievedTask = taskRepository.findTaskByTaskId(taskId);
        assertNotNull(retrievedTask);
        assertEquals("New Test Task", retrievedTask.getTaskName());
        assertEquals("This is a test task description", retrievedTask.getTaskDescription());
        assertEquals(15, retrievedTask.getTaskEstimatedHours());
        assertEquals(TaskStatus.NOT_STARTED, retrievedTask.getTaskStatus());
    }

    @Test
    public void testDeleteTask() {
        // Arrange
        int taskIdToDelete = 3;


        Task taskBeforeDeletion = taskRepository.findTaskByTaskId(taskIdToDelete);
        assertNotNull(taskBeforeDeletion);

        // Act
        taskRepository.deleteTask(taskIdToDelete);

        // Assert
        assertThrows(EmptyResultDataAccessException.class, () -> {
            taskRepository.findTaskByTaskId(taskIdToDelete);
        });
    }

    @Test
    public void testFindTasksBySubProjectId() {
        // Arrange
        int subProjectId = 1;

        // Act
        List<Task> tasks = taskRepository.findTasksBySubProjectId(subProjectId);

        // Assert
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());


        boolean foundErDiagram = false;
        boolean foundDataDictionary = false;

        for (Task task : tasks) {
            assertEquals(subProjectId, task.getSubProjectId());
            if (task.getTaskId() == 1 && "ER Diagram".equals(task.getTaskName())) {
                foundErDiagram = true;
            }
            if (task.getTaskId() == 2 && "Schema Creation".equals(task.getTaskName())) {
                foundDataDictionary = true;
            }
        }

        assertTrue(foundErDiagram, "ER Diagram task should be found");
        assertTrue(foundDataDictionary, "Schema Creation task should be found");
    }

    @Test
    public void testFindTasksBySubProjectId_NonExistentSubProject() {
        // Arrange
        int nonExistentSubProjectId = 9999;

        // Act
        List<Task> tasks = taskRepository.findTasksBySubProjectId(nonExistentSubProjectId);

        // Assert
        assertNotNull(tasks);
        assertTrue(tasks.isEmpty(), "No tasks should be found for non-existent subproject");
    }

    @Test
    public void testFindTaskByTaskId() {
        // Arrange
        int taskId = 1;

        // Act
        Task foundTask = taskRepository.findTaskByTaskId(taskId);

        // Assert
        assertNotNull(foundTask);
        assertEquals(taskId, foundTask.getTaskId());
        assertEquals("ER Diagram", foundTask.getTaskName());
        assertEquals(1, foundTask.getSubProjectId());
    }

    @Test
    public void testFindTaskByTaskId_NonExistentTask() {
        // Arrange
        int nonExistentTaskId = 9999;

        // Act & Assert
        assertThrows(EmptyResultDataAccessException.class, () -> {
            taskRepository.findTaskByTaskId(nonExistentTaskId);
        });
    }

    @Test
    public void testEditTask() {
        // Arrange
        int taskIdToEdit = 4;
        Task taskToEdit = taskRepository.findTaskByTaskId(taskIdToEdit);


        taskToEdit.setTaskName("Updated Testing Plan");
        taskToEdit.setTaskDescription("Updated description for testing plan");
        taskToEdit.setTaskEndDate(taskToEdit.getTaskEndDate().plusDays(5));
        taskToEdit.setTaskEstimatedHours(25);
        taskToEdit.setTaskStatus(TaskStatus.IN_PROGRESS);

        // Act
        taskRepository.editTask(taskToEdit);

        // Assert
        Task updatedTask = taskRepository.findTaskByTaskId(taskIdToEdit);
        assertNotNull(updatedTask);
        assertEquals("Updated Testing Plan", updatedTask.getTaskName());
        assertEquals("Updated description for testing plan", updatedTask.getTaskDescription());
        assertEquals(25.0, updatedTask.getTaskEstimatedHours());
        assertEquals(TaskStatus.IN_PROGRESS, updatedTask.getTaskStatus());
    }

    @Test
    public void testFindAllTasks() {
        // Act
        List<Task> allTasks = taskRepository.findAllTasks();

        // Assert
        assertNotNull(allTasks);
        assertEquals(14, allTasks.size());

        boolean foundTaskOne = false;
        boolean foundTaskTen = false;

        for (Task task : allTasks) {
            if (task.getTaskId() == 1 && "ER Diagram".equals(task.getTaskName())) {
                foundTaskOne = true;
            }
            if (task.getTaskId() == 10 && "Wireframes".equals(task.getTaskName())) {
                foundTaskTen = true;
            }
        }

        assertTrue(foundTaskOne, "Task with ID 1 should exist");
        assertTrue(foundTaskTen, "Task with ID 10 should exist");
    }

    @Test
    public void testAssignEmployeeToTask() {
        // Arrange
        int employeeId = 5;
        int taskId = 3;

        List<Employee> initialAssignedEmployees = taskRepository.findAssignedEmployeesByTaskId(taskId);
        int initialCount = initialAssignedEmployees.size();

        // Act
        taskRepository.assignEmployeeToTask(employeeId, taskId);

        // Assert
        List<Employee> updatedAssignedEmployees = taskRepository.findAssignedEmployeesByTaskId(taskId);
        assertEquals(initialCount + 1, updatedAssignedEmployees.size());

        boolean foundEmployee = false;
        for (Employee employee : updatedAssignedEmployees) {
            if (employee.getEmployeeId() == employeeId) {
                foundEmployee = true;
                assertEquals("David", employee.getFirstname());
                assertEquals("Manager", employee.getLastname());
                break;
            }
        }
        assertTrue(foundEmployee, "Added employee should be found in the assigned employees list");
    }

    @Test
    public void testAssigningTheSameEmployeeToTaskTwice() {
        // Arrange
        int employeeId = 2; // Sara Manager
        int taskId = 5; // API Design task

        taskRepository.assignEmployeeToTask(employeeId, taskId);
        List<Employee> afterFirstAssignment = taskRepository.findAssignedEmployeesByTaskId(taskId);

        // Act
        assertThrows(Exception.class, () -> {
            taskRepository.assignEmployeeToTask(employeeId, taskId);
        });

        // Assert
        List<Employee> afterSecondAssignment = taskRepository.findAssignedEmployeesByTaskId(taskId);
        assertEquals(afterFirstAssignment.size(), afterSecondAssignment.size());
    }

    @Test
    public void testFindAssignedEmployeesByTaskId() {
        // Arrange
        int taskId = 1;

        // Act
        List<Employee> assignedEmployees = taskRepository.findAssignedEmployeesByTaskId(taskId);

        // Assert
        assertNotNull(assignedEmployees);
        assertFalse(assignedEmployees.isEmpty());

        boolean foundJohnAdmin = false;
        for (Employee employee : assignedEmployees) {
            if (employee.getEmployeeId() == 1) {
                foundJohnAdmin = true;
                assertEquals("John", employee.getFirstname());
                assertEquals("Admin", employee.getLastname());
                break;
            }
        }

        assertTrue(foundJohnAdmin, "John Admin should be assigned to task 1");
    }

    @Test
    public void testEdgeCase_FindAssignedEmployeesForTaskWithNoAssignments() {
        // Create a new task with no employees
        Task newTask = new Task();
        newTask.setSubProjectId(1);
        newTask.setTaskName("No Employees Task");
        newTask.setTaskDescription("This task has no assigned employees");
        newTask.setTaskStartDate(LocalDate.now());
        newTask.setTaskEndDate(LocalDate.now().plusDays(10));
        newTask.setTaskEstimatedHours(20);
        newTask.setTaskStatus(TaskStatus.NOT_STARTED);

        int newTaskId = taskRepository.addNewTask(newTask);

        // Act
        List<Employee> assignedEmployees = taskRepository.findAssignedEmployeesByTaskId(newTaskId);

        // Assert
        assertNotNull(assignedEmployees);
        assertTrue(assignedEmployees.isEmpty(), "No employees should be assigned to the new task");
    }

    @Test
    public void testRemoveEmployeeFromTask() {
        // Arrange
        int employeeId = 1;
        int taskId = 1;


        List<Employee> initialAssignedEmployees = taskRepository.findAssignedEmployeesByTaskId(taskId);
        boolean initiallyAssigned = initialAssignedEmployees.stream()
                .anyMatch(employee -> employee.getEmployeeId() == employeeId);
        assertTrue(initiallyAssigned, "Employee should be initially assigned to the task");

        // Act
        taskRepository.removeEmployeeFromTask(employeeId, taskId);

        // Assert
        List<Employee> updatedAssignedEmployees = taskRepository.findAssignedEmployeesByTaskId(taskId);
        boolean stillAssigned = updatedAssignedEmployees.stream()
                .anyMatch(employee -> employee.getEmployeeId() == employeeId);
        assertFalse(stillAssigned, "Employee should no longer be assigned to the task");
    }

    @Test
    public void testEdgeCase_RemoveNonExistentEmployeeTaskAssignment() {
        // Arrange
        int employeeId = 6;
        int taskId = 3;


        List<Employee> initialAssignedEmployees = taskRepository.findAssignedEmployeesByTaskId(taskId);
        boolean initiallyAssigned = initialAssignedEmployees.stream()
                .anyMatch(employee -> employee.getEmployeeId() == employeeId);
        assertFalse(initiallyAssigned, "Employee should not be initially assigned to the task");

        // Act & Assert
        assertDoesNotThrow(() -> {
            taskRepository.removeEmployeeFromTask(employeeId, taskId);
        });


        List<Employee> updatedAssignedEmployees = taskRepository.findAssignedEmployeesByTaskId(taskId);
        assertEquals(initialAssignedEmployees.size(), updatedAssignedEmployees.size());
    }


    @Test
    public void testRegisterHours_NewHoursEntry() {
        // Arrange
        int taskId = 5;
        int hoursToAdd = 8;


        Task taskBefore = taskRepository.findTaskByTaskId(taskId);
        Integer initialHours = taskBefore.getTaskActualHours();
        if (initialHours == null) {
            initialHours = 0;
        }

        // Act
        taskRepository.registerHours(taskId, hoursToAdd);

        // Assert
        Task updatedTask = taskRepository.findTaskByTaskId(taskId);
        assertNotNull(updatedTask);
        assertNotNull(updatedTask.getTaskActualHours());
        assertEquals(initialHours + hoursToAdd, updatedTask.getTaskActualHours());
    }

    @Test
    public void testRegisterHours_AddToExistingHours() {
        // Arrange
        int taskId = 6;


        taskRepository.registerHours(taskId, 5);
        Task taskAfterInitialHours = taskRepository.findTaskByTaskId(taskId);
        int initialHours = taskAfterInitialHours.getTaskActualHours();

        // Act
        int additionalHours = 7;
        taskRepository.registerHours(taskId, additionalHours);

        // Assert
        Task updatedTask = taskRepository.findTaskByTaskId(taskId);
        assertNotNull(updatedTask);
        assertEquals(initialHours + additionalHours, updatedTask.getTaskActualHours());
    }


    @Test
    public void testCalculateTotalTaskEstimatedHours() {
        // Arrange
        int subProjectId = 2;

        // Act
        int totalHours = taskRepository.calculateTotalTaskEstimatedHours(subProjectId);

        // Assert
        assertTrue(totalHours > 0, "Total hours should be greater than zero");


        List<Task> tasksInSubproject = taskRepository.findTasksBySubProjectId(subProjectId);
        double manualSum = 0;
        for (Task task : tasksInSubproject) {
            manualSum += task.getTaskEstimatedHours();
        }

        assertEquals((int)manualSum, totalHours, "Calculated total should match manual calculation");
    }

    @Test
    public void testCalculateTotalTaskEstimatedHours_EmptySubProject() {
        // Arrange
        int nonExistentSubProjectId = 9999;

        // Act
        int totalHours = taskRepository.calculateTotalTaskEstimatedHours(nonExistentSubProjectId);

        // Assert
        assertEquals(0, totalHours, "Total hours should be zero for empty subproject");
    }
}