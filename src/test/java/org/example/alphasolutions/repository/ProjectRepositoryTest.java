package org.example.alphasolutions.repository;

import org.example.alphasolutions.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;


import java.sql.SQLException;
import java.util.List;

@SpringBootTest
@Sql(scripts = "classpath:h2init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testFindAllProjectsCorrectSize() throws SQLException {

        List<Project> projects = projectRepository.findAllProjects();

        assertEquals(3,projects.size(), "Should return the exact number of projects in the database");
    }



}