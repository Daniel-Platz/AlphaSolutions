package org.example.alphasolutions.controller;

import org.example.alphasolutions.enums.Role;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    private Project project1;
    private Project project2;
    private List<Project> allProjects;
    private List<Project> employeeProjects;
    private Employee adminEmployee;
    private Employee managerEmployee;
    private Employee regularEmployee;

    @BeforeEach
    void setUp() {
        project1 = new Project();
        project1.setProjectId(1);
        project1.setProjectName("Test Project 1");

        project2 = new Project();
        project2.setProjectId(2);
        project2.setProjectName("Test Project 2");

        allProjects = Arrays.asList(project1, project2);
        employeeProjects = List.of(project1);

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
    void showProjectsWithNoSessionRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/projects"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void showProjectsAsAdmin() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", adminEmployee);
        session.setAttribute("employeeId", adminEmployee.getEmployeeId());
        session.setAttribute("role", adminEmployee.getRole().toString());

        when(projectService.findAllProjects()).thenReturn(allProjects);

        mockMvc.perform(get("/projects").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("projects"))
                .andExpect(model().attribute("projects", allProjects))
                .andExpect(model().attribute("role", adminEmployee.getRole().toString()));
    }

    @Test
    void showProjectsAsProjectManager() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", managerEmployee);
        session.setAttribute("employeeId", managerEmployee.getEmployeeId());
        session.setAttribute("role", managerEmployee.getRole().toString());

        when(projectService.findAllProjects()).thenReturn(allProjects);

        mockMvc.perform(get("/projects").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("projects"))
                .andExpect(model().attribute("projects", allProjects))
                .andExpect(model().attribute("role", managerEmployee.getRole().toString()));
    }

    @Test
    void showProjectsAsRegularEmployee() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", regularEmployee);
        session.setAttribute("employeeId", regularEmployee.getEmployeeId());
        session.setAttribute("role", regularEmployee.getRole().toString());

        when(projectService.findProjectsByEmployeeId(regularEmployee.getEmployeeId())).thenReturn(employeeProjects);

        mockMvc.perform(get("/projects").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("projects"))
                .andExpect(model().attribute("projects", employeeProjects))
                .andExpect(model().attribute("role", regularEmployee.getRole().toString()));
    }

    @Test
    void showAddProjectForm_ShouldReturnAddProjectView_WithModelAttributes() throws Exception {
        mockMvc.perform(get("/projects/addProject"))
                .andExpect(status().isOk())
                .andExpect(view().name("addProject"))
                .andExpect(model().attributeExists("newProject"))
                .andExpect(model().attributeExists("statuses"));
    }

    @Test
    void saveProject_ShouldRedirectToProjects() throws Exception {
        mockMvc.perform(post("/projects/saveProject")
                .param("projectName", "Test Project")
                .param("projectDescription", "This is a test project")
                .param("projectStartDate", "2025-01-01")
                .param("projectEndDate", "2025-12-31")
                .param("projectEstimatedHours", "200")
                .param("projectStatus", "ACTIVE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
    }
}