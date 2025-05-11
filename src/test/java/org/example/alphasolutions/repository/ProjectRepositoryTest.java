package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.model.SubProject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@Sql(scripts = "classpath:h2init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testFindAllProjectsCorrectSize() throws SQLException {

        List<Project> projects = projectRepository.findAllProjects();

        assertEquals(3, projects.size(), "Should return the exact number of projects in the database");
    }

    @Test
    public void testFindAllProjectCheckAttributes() throws SQLException {

        List<Project> projects = projectRepository.findAllProjects();

        assertNotNull(projects);
        assertFalse(projects.isEmpty());

        Project firstProject = projects.getFirst();
        assertEquals(1, firstProject.getProjectId(), "First project should have ID 1");
        assertEquals("ERP System", firstProject.getProjectName(), "First project should be ERP System");
        assertEquals("Enterprise Resource Planning System Development", firstProject.getProjectDescription(), "Description should match");
        assertEquals(ProjectStatus.ACTIVE, firstProject.getProjectStatus(), "Status should be ACTIVE");
        assertEquals(2000, firstProject.getProjectEstimatedHours(), "Estimated hours should be 2000");

    }

    @Test
    public void testFindProjectsByEmployeeId() {

        int employeeId = 2; // Employee 2 is assigned to projects 1 and 3


        List<Project> projects = projectRepository.findProjectsByEmployeeId(employeeId);


        assertNotNull(projects, "Projects list should not be null");
        assertEquals(2, projects.size(), "Employee 2 should be assigned to 2 projects");

    }

    @Test
    public void testFindProjectsByEmployeeIdWithNoProjects() {
        int nonExistingEmployeeId = 999;

        List<Project> projects = projectRepository.findProjectsByEmployeeId(nonExistingEmployeeId);

        assertNotNull(projects, "Projects list should not be null");
        assertEquals(0, projects.size(), "Non-existent employee should have no projects");
        assertTrue(projects.isEmpty(), "Projects list should be empty");
    }

    @Test
    public void testFindProjectById() {
        Project project = projectRepository.findProjectById(1);

        assertEquals(1, project.getProjectId(), "Project ID should be 1");
        assertEquals("ERP System", project.getProjectName(), "Project name should be ERP System");
        assertEquals("Enterprise Resource Planning System Development", project.getProjectDescription(), "Description should match");
        assertEquals(ProjectStatus.ACTIVE, project.getProjectStatus(), "Status should be ACTIVE");
        assertEquals(2000, project.getProjectEstimatedHours(), "Estimated hours should be 2000");
    }

    @Test
    public void testFindSubProjectsByProjectId() {
        List<SubProject> subProjects = projectRepository.findSubProjectsByProjectId(1);

        assertEquals(4, subProjects.size(), "Project 1 should have 4 subprojects");

        SubProject firstSubProject = subProjects.getFirst();
        assertEquals(1, firstSubProject.getSubProjectId(), "First subproject should have ID 1");
        assertEquals("Database Design", firstSubProject.getSubProjectName(), "First subproject should be Database Design");
        assertEquals("Database schema design and implementation", firstSubProject.getSubProjectDescription(), "Description should match");
    }

    @Test
    public void testFindSubProjectsByProjectIdWithNoSubProjects() {
        List<SubProject> subProjects = projectRepository.findSubProjectsByProjectId(999);

        assertNotNull(subProjects, "Subprojects list should not be null");
        assertEquals(0, subProjects.size(), "Non-existent project should have no subprojects");
        assertTrue(subProjects.isEmpty(), "Subprojects list should be empty");
    }

    @Test
    public void testFindAssignedEmployeesByProjectId() {
        List<Employee> employees = projectRepository.findAssignedEmployeesByProjectId(1);

        assertNotNull(employees, "Employees list should not be null");
        assertEquals(3, employees.size(), "Project 1 should have 3 assigned employees");

        Employee firstEmployee = employees.getFirst();
        assertEquals(1, firstEmployee.getEmployeeId(),"First employee should have ID 1");
        assertEquals("John", firstEmployee.getFirstname(), "First employees firstname should be John");
        assertEquals("Admin", firstEmployee.getLastname(), "First employees lastname should be Admin");
        assertEquals("admin@alphasolutions.com", firstEmployee.getEmail(), "First employees email should be admin@alphasolutions.com");
    }

    @Test
    public void testFindAssignedEmployeesByProjectIdWithNoEmployees() {
        List<Employee> employees = projectRepository.findAssignedEmployeesByProjectId(999);
        assertNotNull(employees, "Employees list should not be null");
        assertEquals(0, employees.size(), "Non-existent project should have no assigned employees");
        assertTrue(employees.isEmpty(), "Employees list should be empty");
    }

    @Test
    @Transactional
    @Rollback
    public void testAddProjectToDB() {
        Project projectToAdd = new Project();
        projectToAdd.setProjectName("Danske Bank ATMs");
        projectToAdd.setProjectDescription("Create a new software for Danske Banks ATMs");
        projectToAdd.setProjectStartDate(LocalDate.now());
        projectToAdd.setProjectEndDate(LocalDate.of(2026, 10, 1));
        projectToAdd.setProjectEstimatedHours(200);
        projectToAdd.setProjectStatus(ProjectStatus.ACTIVE);

        projectRepository.addProjectToDB(projectToAdd);


        List<Project> projects = projectRepository.findAllProjects();

        Project newestAddedProject = projects.getLast();
        assertEquals(4, newestAddedProject.getProjectId(), "First project should have id of 1");
        assertEquals(projectToAdd.getProjectName(), newestAddedProject.getProjectName(), "Name of both objects should be the same");
        assertEquals(projectToAdd.getProjectDescription(), newestAddedProject.getProjectDescription(), "Description of both objects should be the same");
        assertEquals(projectToAdd.getProjectStartDate(), newestAddedProject.getProjectStartDate(), "Start date for both objects should be the same");
        assertEquals(projectToAdd.getProjectEndDate(), newestAddedProject.getProjectEndDate(), "End date for both objects should be the same");
        assertEquals(projectToAdd.getProjectEstimatedHours(), newestAddedProject.getProjectEstimatedHours(), "Estimated hours for both objects should be equal");
        assertEquals(projectToAdd.getProjectStatus(), newestAddedProject.getProjectStatus(), "Status for both objects should be the same");
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteProjectFromDB(){
        List<Project> projectsBeforeDeletion = projectRepository.findAllProjects();

        projectRepository.deleteProjectFromDB(1);

        List<Project> projectsAfterDeletion = projectRepository.findAllProjects();

        assertNotEquals(projectsBeforeDeletion.size(), projectsAfterDeletion.size(), "The size of the two lists should not be the same");
        assertNotEquals(projectsBeforeDeletion.getFirst().getProjectName(), projectsAfterDeletion.getFirst().getProjectName(),
                "The project name of the first project in each list should not be the same, as it should've been deleted in one of them");
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateProject(){
        Project projectToChange = projectRepository.findProjectById(1);
        int oldManagerId = projectToChange.getManagerId();

        projectToChange.setProjectName("test");
        projectToChange.setProjectDescription("test description");
        projectToChange.setProjectStartDate(LocalDate.of(2025, 7, 10));
        projectToChange.setProjectEndDate(LocalDate.of(2025, 10, 20));
        projectToChange.setProjectEstimatedHours(1000);
        projectToChange.setProjectStatus(ProjectStatus.ON_HOLD);
        projectToChange.setManagerId(5);

        projectRepository.updateProject(projectToChange, oldManagerId);

        Project updatedProject = projectRepository.findProjectById(1);

        assertEquals(1, updatedProject.getProjectId(), "Edited project should have the same id at old project.");
        assertEquals("test", updatedProject.getProjectName(), "The updated project name should be test");
        assertNotEquals("ERP System", updatedProject.getProjectName(), "Updated project should no longer be called ERP System");
        assertEquals("test description", updatedProject.getProjectDescription(), "New description should be: test description");
        assertNotEquals("Enterprise Resource Planning System Development", updatedProject.getProjectDescription(),"Project description should no longer be: Enterprise Resource Planning System Development");
        assertEquals(LocalDate.of(2025, 7, 10), updatedProject.getProjectStartDate(), "new start date should be 2025-7-10");
        assertEquals(LocalDate.of(2025, 10,20), updatedProject.getProjectEndDate(), "new end date should be 2025-10-20");
        assertEquals(1000, updatedProject.getProjectEstimatedHours(), "new project estimate should be 1000 hours");
        assertNotEquals(2000, updatedProject.getProjectEstimatedHours(),"project estimated hours should no longer be 2000");
        assertEquals(ProjectStatus.ON_HOLD, updatedProject.getProjectStatus(), "new status should be: ON_HOLD");
        assertEquals(5, updatedProject.getManagerId(), "new managerId should be 5");
    }
}