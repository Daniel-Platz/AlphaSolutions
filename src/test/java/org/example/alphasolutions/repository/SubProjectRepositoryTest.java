package org.example.alphasolutions.repository;


import org.example.alphasolutions.enums.ProjectStatus;
import org.example.alphasolutions.model.SubProject;
import org.example.alphasolutions.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
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

    @Test
    void testCalculateTotalSubProjectEstimatedHours() {
        int projectId = 1;

        int totalHours = subProjectRepository.calculateTotalSubProjectEstimatedHours(projectId);

        assertTrue(totalHours>=0);
    }

    @Test
    void testCalculateTotalSubProjectEstimatedHoursNoSubProjects() {
        int projectId = 999;

        int totalHours = subProjectRepository.calculateTotalSubProjectEstimatedHours(projectId);

        assertEquals(0, totalHours);
    }

    @Test
    void testGetTotalSubProjectEstimatedHoursMultipleSubProjects() {
        int projectId = 1;
        SubProject subProject1 = new SubProject();
        subProject1.setProjectId(projectId);
        subProject1.setSubProjectName("Test SubProject 1");
        subProject1.setSubProjectDescription("Description 1");
        subProject1.setSubProjectStartDate(LocalDate.now());
        subProject1.setSubProjectEndDate(LocalDate.now().plusDays(10));
        subProject1.setSubProjectEstimatedHours(100);
        subProject1.setSubProjectStatus(ProjectStatus.ACTIVE);

        SubProject subProject2 = new SubProject();
        subProject2.setProjectId(projectId);
        subProject2.setSubProjectName("Test SubProject 2");
        subProject2.setSubProjectDescription("Description 2");
        subProject2.setSubProjectStartDate(LocalDate.now());
        subProject2.setSubProjectEndDate(LocalDate.now().plusDays(15));
        subProject2.setSubProjectEstimatedHours(200);
        subProject2.setSubProjectStatus(ProjectStatus.ACTIVE);

        int initialTotal = subProjectRepository.calculateTotalSubProjectEstimatedHours(projectId);

        subProjectRepository.addNewSubProject(subProject1);
        subProjectRepository.addNewSubProject(subProject2);

        int newTotal = subProjectRepository.calculateTotalSubProjectEstimatedHours(projectId);

        assertEquals(initialTotal + 300, newTotal, "Total should include hours from both new subprojects");
    }

        @Test
    public void testAddNewSubProject() {
        SubProject subProjectToAdd = new SubProject();
        subProjectToAdd.setProjectId(1);
        subProjectToAdd.setSubProjectName("Test SubProject");
        subProjectToAdd.setSubProjectDescription("This is a test description");
        subProjectToAdd.setSubProjectStartDate(LocalDate.now());
        subProjectToAdd.setSubProjectEndDate(LocalDate.now().plusDays(10));
        subProjectToAdd.setSubProjectEstimatedHours(50);
        subProjectToAdd.setSubProjectStatus(ProjectStatus.ACTIVE);

        int newSubProjectId = subProjectRepository.addNewSubProject(subProjectToAdd);

        assertTrue(newSubProjectId > 0, "Should return a valid ID");

        List<SubProject> subProjects = subProjectRepository.findAllSubProjects();

        SubProject createdSubProject = null;
        for (SubProject sp : subProjects) {
            if ("Test SubProject".equals(sp.getSubProjectName())) {
                createdSubProject = sp;
                break;
            }
        }
        assertNotNull(createdSubProject);
        assertEquals("This is a test description", createdSubProject.getSubProjectDescription(), "Description should match");
        assertEquals(50, createdSubProject.getSubProjectEstimatedHours(),
                "Estimated hours should match");
        assertEquals(ProjectStatus.ACTIVE, createdSubProject.getSubProjectStatus(),
                "Status should match");
    }

    @Test
    void testDeleteSubProject() {
        int subProjectId = 1;

        List<Task> tasksBeforeDelete = subProjectRepository.findTasksBySubProjectId(subProjectId);
        assertEquals(3, tasksBeforeDelete.size());

        subProjectRepository.deleteSubProject(subProjectId);

        List<Task> tasksAfterDelete = subProjectRepository.findTasksBySubProjectId(subProjectId);

        assertEquals(0, tasksAfterDelete.size());
    }

    @Test
    void testEditSubProject() {
        SubProject createdSubProject = new SubProject();
        createdSubProject.setProjectId(1);
        createdSubProject.setSubProjectName("Test SubProject");
        createdSubProject.setSubProjectDescription("This is a test description");
        createdSubProject.setSubProjectStartDate(LocalDate.now());
        createdSubProject.setSubProjectEndDate(LocalDate.now().plusDays(10));
        createdSubProject.setSubProjectEstimatedHours(50);
        createdSubProject.setSubProjectStatus(ProjectStatus.ACTIVE);

        int subProjectId = subProjectRepository.addNewSubProject(createdSubProject);

        SubProject subProjectToEdit = new SubProject();
        subProjectToEdit.setSubProjectId(subProjectId);
        subProjectToEdit.setSubProjectName("Edited SubProject");
        subProjectToEdit.setSubProjectDescription("Edited description");
        subProjectToEdit.setSubProjectStartDate(LocalDate.now().plusDays(1));
        subProjectToEdit.setSubProjectEndDate(LocalDate.now().plusDays(15));
        subProjectToEdit.setSubProjectEstimatedHours(100);
        subProjectToEdit.setSubProjectStatus(ProjectStatus.COMPLETED);

        subProjectRepository.editSubProject(subProjectToEdit);

        SubProject editedSubProject = subProjectRepository.findSubProjectById(subProjectId);
        assertNotNull(editedSubProject);
        assertEquals("Edited SubProject", editedSubProject.getSubProjectName());
        assertEquals("Edited description", editedSubProject.getSubProjectDescription());
        assertEquals(LocalDate.now().plusDays(1), editedSubProject.getSubProjectStartDate());
        assertEquals(LocalDate.now().plusDays(15), editedSubProject.getSubProjectEndDate());
        assertEquals(100, editedSubProject.getSubProjectEstimatedHours());
        assertEquals(ProjectStatus.COMPLETED, editedSubProject.getSubProjectStatus());
    }
}