package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


import java.sql.SQLException;
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

        assertEquals(3,projects.size(), "Should return the exact number of projects in the database");
    }

    @Test
    public void testFindAllProjectCheckAttributes() throws  SQLException {

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

        Integer employeeId = 2; // Employee 2 (Sara Manager) is assigned to projects 1 and 3


        List<Project> projects = projectRepository.findProjectsByEmployeeId(employeeId);


        assertNotNull(projects, "Projects list should not be null");
        assertEquals(2, projects.size(), "Employee 2 should be assigned to 2 projects");

    }
}