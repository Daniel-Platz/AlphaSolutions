package org.example.alphasolutions.controller;

import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.enums.Role;
import org.example.alphasolutions.enums.TaskStatus;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.example.alphasolutions.service.SubProjectService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubProjectController.class)
class SubProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SubProjectService subProjectService;

    private Task task1;
    private Task task2;
    private List<Task> subProjectTasks;
    private Employee adminEmployee;
    private Employee managerEmployee;
    private Employee regularEmployee;
    private SubProject subProject;

    @BeforeEach
    void setUp() {

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

        subProject = new SubProject();
        subProject.setSubProjectId(1);
        subProject.setSubProjectName("Test SubProject");
        subProject.setSubProjectDescription("Test Description");
        subProject.setProjectId(1);
        subProject.setSubProjectStatus(ProjectStatus.ACTIVE);
    }

    @Test
    void showSubProjectOverviewWithNoSessionRedirectsToLogin() throws Exception {
        int projectId = 1;
        int subProjectId = 1;

        mockMvc.perform(get("/projects/{projectId}/subprojects/{subProjectId}", projectId, subProjectId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void showSubProjectOverviewAsAdmin() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        int totalHours = 180;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", adminEmployee);
        session.setAttribute("employeeId", adminEmployee.getEmployeeId());
        session.setAttribute("role", adminEmployee.getRole().toString());

        when(subProjectService.findTasksBySubProjectId(subProjectId)).thenReturn(subProjectTasks);
        when(subProjectService.calculateSubProjectTotalHours(subProjectId)).thenReturn(totalHours);

        mockMvc.perform(get("/projects/{projectId}/subprojects/{subProjectId}", projectId, subProjectId)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("subProjectOverview"))
                .andExpect(model().attribute("tasks", subProjectTasks))
                .andExpect(model().attribute("subProjectId", subProjectId))
                .andExpect(model().attribute("totalHours", totalHours))
                .andExpect(model().attribute("role", adminEmployee.getRole().toString()));
    }

    @Test
    void showSubProjectOverviewAsProjectManager() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        int totalHours = 180;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", managerEmployee);
        session.setAttribute("employeeId", managerEmployee.getEmployeeId());
        session.setAttribute("role", managerEmployee.getRole().toString());

        when(subProjectService.findTasksBySubProjectId(subProjectId)).thenReturn(subProjectTasks);
        when(subProjectService.calculateSubProjectTotalHours(subProjectId)).thenReturn(totalHours);

        mockMvc.perform(get("/projects/{projectId}/subprojects/{subProjectId}", projectId, subProjectId)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("subProjectOverview"))
                .andExpect(model().attribute("tasks", subProjectTasks))
                .andExpect(model().attribute("subProjectId", subProjectId))
                .andExpect(model().attribute("totalHours", totalHours))
                .andExpect(model().attribute("role", managerEmployee.getRole().toString()));
    }

    @Test
    void showSubProjectOverviewAsRegularEmployee() throws Exception {
        int projectId = 1;
        int subProjectId = 1;
        int totalHours = 180;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", regularEmployee);
        session.setAttribute("employeeId", regularEmployee.getEmployeeId());
        session.setAttribute("role", regularEmployee.getRole().toString());

        when(subProjectService.findTasksBySubProjectId(subProjectId)).thenReturn(subProjectTasks);
        when(subProjectService.calculateSubProjectTotalHours(subProjectId)).thenReturn(totalHours);

        mockMvc.perform(get("/projects/{projectId}/subprojects/{subProjectId}", projectId, subProjectId)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("subProjectOverview"))
                .andExpect(model().attribute("tasks", subProjectTasks))
                .andExpect(model().attribute("subProjectId", subProjectId))
                .andExpect(model().attribute("totalHours", totalHours))
                .andExpect(model().attribute("role", regularEmployee.getRole().toString()));
    }

    @Test
    void addNewSubProjectTest() throws Exception {
        int projectId = 1;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", adminEmployee);
        session.setAttribute("employeeId", adminEmployee.getEmployeeId());
        session.setAttribute("role", adminEmployee.getRole().toString());

        mockMvc.perform(get("/dashboard/{projectId}/projectOverview/addSubproject", projectId)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("addSubProject"))
                .andExpect(model().attributeExists("newSubProject"))
                .andExpect(model().attribute("projectId", projectId))
                .andExpect(model().attributeExists("statuses"));
    }

    @Test
    void saveSubProjectTest() throws Exception {
        int projectId = 1;
        int newSubProjectId = 1;

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", adminEmployee);
        session.setAttribute("employeeId", adminEmployee.getEmployeeId());
        session.setAttribute("role", adminEmployee.getRole().toString());

        when(subProjectService.addNewSubProject(any(SubProject.class))).thenReturn(newSubProjectId);

        mockMvc.perform(post("/projects/{projectId}/subprojects/saveSubProject", projectId)
                        .session(session)
                        .param("subProjectName", "New SubProject")
                        .param("subProjectDescription", "New Description")
                        .param("subProjectStatus", "ACTIVE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/" + projectId + "/overview"));
    }
}