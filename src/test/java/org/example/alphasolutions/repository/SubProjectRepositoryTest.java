package org.example.alphasolutions.repository;


import org.example.alphasolutions.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:h2init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SubProjectRepositoryTest {


    @Autowired
    private SubProjectRepository subProjectRepository;

    @Test
    void testGetTaskBySubProjectId() {
        int subProjectId = 1;

       List<Task> tasks = subProjectRepository.findTasksBySubProjectId(subProjectId);

       assertEquals(3, tasks.size());
    }

    @Test
    void testGetTaskBySubProjectIdNoTasksFound() {
        int subProjectId = 999;

        List<Task> tasks = subProjectRepository.findTasksBySubProjectId(subProjectId);

        assertNotNull(tasks);
        assertEquals(0, tasks.size());
    }
}