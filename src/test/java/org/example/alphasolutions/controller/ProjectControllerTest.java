package org.example.alphasolutions.controller;

import org.example.alphasolutions.model.Project;
import org.example.alphasolutions.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;


@WebMvcTest (ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    private Project project1;
    private Project project2;
    private List<Project> allProjects;
    private List<Project> employeeProjects;

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
    }





}