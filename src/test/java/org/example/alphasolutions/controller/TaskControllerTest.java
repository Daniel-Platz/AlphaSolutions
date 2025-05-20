package org.example.alphasolutions.controller;

import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.enums.Role;
import org.example.alphasolutions.enums.TaskStatus;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.service.SubProjectService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    private Task task1;
    private Task task2;
    private List<Task> subProjectTasks;
    private SubProject subProject;
    private Employee adminEmployee;
    private Employee managerEmployee;
    private Employee regularEmployee;


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

        subProject = new SubProject();
        subProject.setSubProjectId(1);
        subProject.setSubProjectName("Test SubProject");
        subProject.setSubProjectDescription("Test Description");
        subProject.setProjectId(1);
        subProject.setSubProjectStatus(ProjectStatus.ACTIVE);


    }

    @Test
    void addNewTaskTest () throws Exception {
        int projectId = 1;
        int subProjectId = 1;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", adminEmployee);
        session.setAttribute("employeeId", adminEmployee.getEmployeeId());
        session.setAttribute("role", adminEmployee.getRole().toString());

        mockMvc.perform(get("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview/addTask", projectId, subProjectId)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("addTask"))
                .andExpect(model().attributeExists("newTask"))
                .andExpect(model().attribute("projectId", projectId))
                .andExpect(model().attribute("subProjectId", subProjectId))
                .andExpect(model().attributeExists("statuses"));


    }

    @Test
    void deleteTaskTest() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        int taskId = 1;

        mockMvc.perform(post("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview/{taskId}/deleteTask"
                ,projectId, subProjectId, taskId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/" + projectId + "/projectOverview/" + subProjectId + "/subProjectOverview"));
    }



    @Test
    void editTaskWithoutSessionRedirectsToLogin() throws Exception {
        int projectId = 1;
        int subProjectId = 5;
        int taskId = 2;

        mockMvc.perform(get("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview/{taskId}/editTask",
                        projectId, subProjectId, taskId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }


    @Test
    void UpdateExistingTask() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        int taskID = 1;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", adminEmployee);
        session.setAttribute("employeeId", adminEmployee.getEmployeeId());
        session.setAttribute("role", adminEmployee.getRole().toString());

        mockMvc.perform(post("/dashboard/{projectId}/projectOverview/{subProjectId}/subProjectOverview/{taskId}/updateTask"
                        ,projectId, subProjectId, taskID)
                        .session(session)
                        .param("subProjectId", String.valueOf(subProjectId))
                        .param("taskId", String.valueOf(taskID))
                        .param("taskName", "Updated Task")
                        .param("taskDescription", "Updated Description")
                        .param("taskStatus", "COMPLETED"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard/" + projectId + "/projectOverview/" + subProjectId + "/subProjectOverview"));
    }



}