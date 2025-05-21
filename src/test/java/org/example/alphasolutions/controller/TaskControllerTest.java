
package org.example.alphasolutions.controller;

import org.example.alphasolutions.enums.Role;
import org.example.alphasolutions.enums.TaskStatus;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.service.ProjectService;
import org.example.alphasolutions.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;



import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private ProjectService projectService;

    private Task task1;
    private Task task2;
    private List<Task> subProjectTasks;
    private Employee adminEmployee;
    private Employee managerEmployee;
    private Employee regularEmployee;
    private List<Employee> projectEmployees;
    private List<Employee> taskEmployees;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        adminEmployee = new Employee();
        adminEmployee.setEmployeeId(1);
        adminEmployee.setFirstname("Admin");
        adminEmployee.setLastname("User");
        adminEmployee.setRole(Role.ADMIN);

        managerEmployee = new Employee();
        managerEmployee.setEmployeeId(2);
        managerEmployee.setFirstname("Project");
        managerEmployee.setLastname("Manager");
        managerEmployee.setRole(Role.PROJECT_MANAGER);

        regularEmployee = new Employee();
        regularEmployee.setEmployeeId(3);
        regularEmployee.setFirstname("Regular");
        regularEmployee.setLastname("Employee");
        regularEmployee.setRole(Role.EMPLOYEE);

        task1 = new Task();
        task1.setTaskId(1);
        task1.setTaskName("Test Task 1");
        task1.setTaskDescription("Description for test task 1");
        task1.setTaskStartDate(LocalDate.of(2025, 1, 15));
        task1.setTaskEndDate(LocalDate.of(2025, 1, 31));
        task1.setTaskEstimatedHours(80);
        task1.setTaskStatus(TaskStatus.COMPLETED);
        task1.setSubProjectId(1);

        task2 = new Task();
        task2.setTaskId(2);
        task2.setTaskName("Test Task 2");
        task2.setTaskDescription("Description for test task 2");
        task2.setTaskStartDate(LocalDate.of(2025, 2, 1));
        task2.setTaskEndDate(LocalDate.of(2025, 2, 15));
        task2.setTaskEstimatedHours(100);
        task2.setTaskStatus(TaskStatus.IN_PROGRESS);
        task2.setSubProjectId(1);

        subProjectTasks = Arrays.asList(task1, task2);

        projectEmployees = Arrays.asList(adminEmployee, managerEmployee, regularEmployee);
        taskEmployees = Arrays.asList(adminEmployee);

        session = new MockHttpSession();
        session.setAttribute("employee", adminEmployee);
        session.setAttribute("employeeId", adminEmployee.getEmployeeId());
        session.setAttribute("role", adminEmployee.getRole().toString());
    }

    @Test
    void showTaskOverviewTest() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        int taskId = 1;

        // Set up mock responses
        when(taskService.findTaskByTaskId(taskId)).thenReturn(task1);
        when(projectService.findAssignedEmployeesByProjectId(projectId)).thenReturn(projectEmployees);
        when(taskService.findAssignedEmployeesByTaskId(taskId)).thenReturn(taskEmployees);

        mockMvc.perform(get("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview/{taskId}/taskOverview",
                        projectId, subProjectId, taskId)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("taskOverview"))
                .andExpect(model().attributeExists("task"))
                .andExpect(model().attributeExists("projectId"))
                .andExpect(model().attributeExists("subProjectId"))
                .andExpect(model().attributeExists("taskId"))
                .andExpect(model().attributeExists("taskEmployees"))
                .andExpect(model().attributeExists("projectEmployees"))
                .andExpect(model().attributeExists("availableEmployees"))
                .andExpect(model().attributeExists("role"));
    }

    @Test
    void showTaskOverviewWithoutSessionRedirectsToLogin() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        int taskId = 1;

        mockMvc.perform(get("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview/{taskId}/taskOverview",
                        projectId, subProjectId, taskId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void createTaskTest() throws Exception {
        int projectId = 1;
        int subProjectId = 1;

        mockMvc.perform(post("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview/saveTask",
                        projectId, subProjectId)
                        .param("taskName", "New Task")
                        .param("taskDescription", "New Task Description")
                        .param("taskEstimatedHours", "40")
                        .param("taskStatus", "NOT_STARTED")
                        .param("taskStartDate", "2025-06-01")
                        .param("taskEndDate", "2025-06-15"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/" + projectId + "/projectOverview/" + subProjectId + "/subProjectOverview"));
    }

    @Test
    void editTaskTest() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        int taskId = 1;

        when(taskService.findTaskByTaskId(taskId)).thenReturn(task1);

        mockMvc.perform(get("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview/{taskId}/editTask",
                        projectId, subProjectId, taskId)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("editTask"))
                .andExpect(model().attributeExists("projectId"))
                .andExpect(model().attributeExists("statuses"))
                .andExpect(model().attributeExists("task"));
    }

    @Test
    void addEmployeeToTaskTest() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        int taskId = 1;
        int employeeId = 2;

        mockMvc.perform(post("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview/{taskId}/taskOverview/addEmployee",
                        projectId, subProjectId, taskId)
                        .session(session)
                        .param("employeeId", String.valueOf(employeeId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/" + projectId + "/projectOverview/" + subProjectId +
                        "/subProjectOverview/" + taskId + "/taskOverview"));
    }

    @Test
    void addEmployeeToTaskWithoutSessionRedirectsToLogin() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        int taskId = 1;
        int employeeId = 2;

        mockMvc.perform(post("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview/{taskId}/taskOverview/addEmployee",
                        projectId, subProjectId, taskId)
                        .param("employeeId", String.valueOf(employeeId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void removeEmployeeFromTaskTest() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        int taskId = 1;
        int employeeId = 1;

        mockMvc.perform(post("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview/{taskId}/employees/{employeeId}/remove",
                        projectId, subProjectId, taskId, employeeId)
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/" + projectId + "/projectOverview/" + subProjectId +
                        "/subProjectOverview/" + taskId + "/taskOverview"));
    }

    @Test
    void removeEmployeeFromTaskWithoutSessionRedirectsToLogin() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        int taskId = 1;
        int employeeId = 1;

        mockMvc.perform(post("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview/{taskId}/employees/{employeeId}/remove",
                        projectId, subProjectId, taskId, employeeId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}