package org.example.alphasolutions.controller;

import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.enums.Role;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.service.EmployeeService;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
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

    @MockitoBean
    EmployeeService employeeService;

    private Project project1;
    private Project project2;
    private List<Project> allProjects;
    private List<Project> employeeProjects;
    private Employee adminEmployee;
    private Employee managerEmployee;
    private Employee regularEmployee;
    private List<SubProject> subProjects;
    private List<Employee> assignedEmployees;

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

        subProjects = List.of(
                new SubProject()
        );

        assignedEmployees = List.of(
                managerEmployee,
                regularEmployee
        );
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
    void showProjectOverviewWithNonExistentProjectRedirectsToProjects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", adminEmployee);
        session.setAttribute("employeeId", adminEmployee.getEmployeeId());
        session.setAttribute("role", adminEmployee.getRole().toString());

        when(projectService.findProjectById(anyInt())).thenReturn(null);

        mockMvc.perform(get("/projects/999/overview").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
    }

    @Test
    void showProjectOverviewReturnsProjectOverview() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", adminEmployee);
        session.setAttribute("employeeId", adminEmployee.getEmployeeId());
        session.setAttribute("role", adminEmployee.getRole().toString());

        when(projectService.findProjectById(1)).thenReturn(project1);
        when(projectService.findSubProjectsByProjectId(1)).thenReturn(subProjects);
        when(projectService.findAssignedEmployeesByProjectId(1)).thenReturn(assignedEmployees);

        mockMvc.perform(get("/projects/1/overview").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("projectOverview"))
                .andExpect(model().attribute("project", project1))
                .andExpect(model().attribute("subProjects", subProjects))
                .andExpect(model().attribute("assignedEmployees", assignedEmployees))
                .andExpect(model().attribute("statuses", ProjectStatus.values()))
                .andExpect(model().attribute("role", adminEmployee.getRole().toString()));
    }

    @Test
    void showAddProjectForm_ShouldReturnAddProjectView_WithModelAttributes() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", adminEmployee);
        session.setAttribute("employeeId", adminEmployee.getEmployeeId());
        session.setAttribute("role", adminEmployee.getRole().toString());

        mockMvc.perform(get("/projects/addProject")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("addProject"))
                .andExpect(model().attributeExists("newProject"))
                .andExpect(model().attributeExists("statuses"));
    }

    @Test
    void saveProject_ShouldRedirectToProjects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", adminEmployee);
        session.setAttribute("employeeId", adminEmployee.getEmployeeId());
        session.setAttribute("role", adminEmployee.getRole().toString());

        mockMvc.perform(post("/projects/saveProject")
                        .session(session)
                        .param("managerId", "1")
                        .param("projectName", "Test Project")
                        .param("projectDescription", "This is a test project")
                        .param("projectStartDate", "2025-01-01")
                        .param("projectEndDate", "2025-12-31")
                        .param("projectEstimatedHours", "200")
                        .param("projectStatus", "ACTIVE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
    }

    @Test
    void deleteProject_ShouldRedirectToProjects() throws Exception {
        mockMvc.perform(post("/projects/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
    }

    @Test
    void showEditProjectForm_shouldReturnEditProjectView_withModelAttributes() throws Exception {
        Project mockProject = new Project();

        mockProject.setProjectId(1);
        mockProject.setProjectName("mock name");
        mockProject.setProjectDescription("mock description");
        mockProject.setProjectStartDate(LocalDate.of(2025, 1, 1));
        mockProject.setProjectEndDate(LocalDate.of(2025, 12, 31));
        mockProject.setProjectEstimatedHours(5000);
        mockProject.setProjectStatus(ProjectStatus.ACTIVE);
        mockProject.setManagerId(2);

        List<Employee> mockListOfManagers = new ArrayList<>();
        Employee employee1 = new Employee(1, "Jack", "Jensen", "jack@alphasolutions.dk", Role.EMPLOYEE, "hejsa1234");
        Employee employee2 = new Employee(2, "Test", "Testen", "test@alphasolutions.dk", Role.PROJECT_MANAGER, "hejsa1234");
        mockListOfManagers.add(employee1);
        mockListOfManagers.add(employee2);

        when(projectService.findProjectById(1)).thenReturn(mockProject);
        when(employeeService.getAllManagers()).thenReturn(mockListOfManagers);

        mockMvc.perform(get("/projects/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("editProject"))
                .andExpect(model().attribute("project", mockProject))
                .andExpect(model().attribute("statuses", ProjectStatus.values()))
                .andExpect(model().attribute("managers", mockListOfManagers))
                .andExpect(model().attribute("oldManagerId", 2));
    }

    @Test
    void saveEditProject_shouldUpdateProjectAndRedirect() throws Exception {
        int projectId = 1;
        int oldManagerId = 2;

        mockMvc.perform(post("/projects/{projectId}/edit", projectId)
                        .param("projectName", "updated name")
                        .param("projectDescription", "updated Description")
                        .param("projectStartDate", "2025-01-01")
                        .param("projectEndDate", "2025-12-31")
                        .param("projectEstimatedHours", "1000")
                        .param("projectStatus", "ACTIVE")
                        .param("managerId", "3")
                        .param("oldManagerId", String.valueOf(oldManagerId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
    }
}