package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.TaskStatus;
import org.example.alphasolutions.model.Employee;
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
    public void testFindAllTasks() {
        // Act
        List<Task> allTasks = taskRepository.findAllTasks();

        // Assert
        assertNotNull(allTasks);
        // Based on the h2init.sql, there should be 14 tasks in total
        assertEquals(14, allTasks.size());

        // Verify some tasks from the init script exist
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
        int employeeId = 5; // David Manager
        int taskId = 3; // Data Migration Plan

        // Get initial employee count for task
        List<Employee> initialAssignedEmployees = taskRepository.findAssignedEmployeesByTaskId(taskId);
        int initialCount = initialAssignedEmployees.size();

        // Act
        taskRepository.assignEmployeeToTask(employeeId, taskId);

        // Assert
        List<Employee> updatedAssignedEmployees = taskRepository.findAssignedEmployeesByTaskId(taskId);
        assertEquals(initialCount + 1, updatedAssignedEmployees.size());

        // Verify the added employee exists in the list
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
    public void testFindAssignedEmployeesByTaskId() {
        // Arrange
        int taskId = 1; // ER Diagram task which has employee 1 assigned

        // Act
        List<Employee> assignedEmployees = taskRepository.findAssignedEmployeesByTaskId(taskId);

        // Assert
        assertNotNull(assignedEmployees);
        assertFalse(assignedEmployees.isEmpty());

        // Based on init script, employee 1 (John Admin) should be assigned to task 1
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
    public void testRemoveEmployeeFromTask() {
        // Arrange
        int employeeId = 1; // John Admin
        int taskId = 1; // ER Diagram

        // Verify employee is initially assigned
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
    public void testEdgeCase_RemoveNonExistentEmployeeTaskAssignment() {
        // Arrange - assuming employee 6 is not assigned to task 3
        int employeeId = 6;
        int taskId = 3;

        // Verify employee is not initially assigned
        List<Employee> initialAssignedEmployees = taskRepository.findAssignedEmployeesByTaskId(taskId);
        boolean initiallyAssigned = initialAssignedEmployees.stream()
                .anyMatch(employee -> employee.getEmployeeId() == employeeId);
        assertFalse(initiallyAssigned, "Employee should not be initially assigned to the task");

        // Act & Assert - this should not throw an exception
        assertDoesNotThrow(() -> {
            taskRepository.removeEmployeeFromTask(employeeId, taskId);
        });

        // Employees list should remain unchanged
        List<Employee> updatedAssignedEmployees = taskRepository.findAssignedEmployeesByTaskId(taskId);
        assertEquals(initialAssignedEmployees.size(), updatedAssignedEmployees.size());
    }

    @Test
    public void testAssigningTheSameEmployeeToTaskTwice() {
        // Arrange
        int employeeId = 2; // Sara Manager
        int taskId = 5; // API Design task

        // First assignment
        taskRepository.assignEmployeeToTask(employeeId, taskId);
        List<Employee> afterFirstAssignment = taskRepository.findAssignedEmployeesByTaskId(taskId);

        // Act - try to assign the same employee again
        // This test checks if the database constraint prevents duplicate entries
        assertThrows(Exception.class, () -> {
            taskRepository.assignEmployeeToTask(employeeId, taskId);
        });

        // Assert - employee count should remain the same
        List<Employee> afterSecondAssignment = taskRepository.findAssignedEmployeesByTaskId(taskId);
        assertEquals(afterFirstAssignment.size(), afterSecondAssignment.size());
    }
}