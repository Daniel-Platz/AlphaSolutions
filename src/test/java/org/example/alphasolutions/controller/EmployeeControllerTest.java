package org.example.alphasolutions.controller;

import org.example.alphasolutions.enums.Role;
import org.example.alphasolutions.exception.InvalidCredentialsException;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee(1, "testFirstname", "testLastname", "test@mail.com", Role.EMPLOYEE);
    }

    @Test
    public void homeRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void showLoginPageReturnsLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void loginSuccessRedirectsToProjects() throws Exception {
        when(employeeService.findByEmailAndPassword("test@mail.com", "password")).thenReturn(employee);

        mockMvc.perform(post("/login")
                        .param("email", "test@mail.com")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"))
                .andExpect(request().sessionAttribute("employee", employee))
                .andExpect(request().sessionAttribute("employeeId", 1))
                .andExpect(request().sessionAttribute("role", "EMPLOYEE"));
    }

    @Test
    public void loginFailureReturnsLoginViewWithError() throws Exception {
        when(employeeService.findByEmailAndPassword("test@mail.com", "wrongpassword"))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/login")
                        .param("email", "test@mail.com")
                        .param("password", "wrongpassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Forkert email eller adgangskode. Hvis du har glemt din adgangskode, kontakt venligst en administrator."));
    }

    @Test
    void logoutInvalidatesSessionAndRedirectsToLogin() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", employee);
        session.setAttribute("employeeId", 1);
        session.setAttribute("role", "EMPLOYEE");


        mockMvc.perform(post("/logout")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

}