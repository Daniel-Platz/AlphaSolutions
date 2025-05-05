package org.example.alphasolutions.repository;

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
        assertEquals(1,firstProject.getProjectId());
        assertEquals("ERP System", firstProject.getProjectName());

    }

}