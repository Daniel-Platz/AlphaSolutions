package org.example.alphasolutions.controller;

import org.example.alphasolutions.enums.Role;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

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

}