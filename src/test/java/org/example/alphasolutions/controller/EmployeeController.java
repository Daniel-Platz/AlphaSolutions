package org.example.alphasolutions.controller;

import org.example.alphasolutions.enums.Role;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
        employee = new Employee(1, "testFirstname", "testLastname", "test@mail.com", Role.EMPLOYEE, "testpassword123");
    }


    @Test
    void showEmployeeManagementAsAdminShowsView() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "ADMIN");

        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        mockMvc.perform(get("/admin/employees").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-management"))
                .andExpect(model().attributeExists("employees"));
    }

    @Test
    void showEmployeeManagementAsNonAdminRedirects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "EMPLOYEE");

        mockMvc.perform(get("/admin/employees").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
    }

    @Test
    void addEmployeeAsAdminSuccess() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "ADMIN");

        mockMvc.perform(post("/admin/employees/add")
                        .session(session)
                        .param("firstname", "New")
                        .param("lastname", "User")
                        .param("email", "newuser")
                        .param("role", "EMPLOYEE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));

        verify(employeeService, times(1)).addEmployee(any(Employee.class));
    }

    @Test
    void addEmployeeAsNonAdminRedirects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "EMPLOYEE");

        mockMvc.perform(post("/admin/employees/add")
                        .session(session)
                        .param("firstname", "New")
                        .param("lastname", "User")
                        .param("email", "newuser")
                        .param("role", "EMPLOYEE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));

        verify(employeeService, never()).addEmployee(any(Employee.class));
    }

    @Test
    void deleteEmployeeAsAdminSuccess() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "ADMIN");

        mockMvc.perform(post("/admin/employees/delete")
                        .session(session)
                        .param("employeeId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));

        verify(employeeService).deleteEmployeeById(1);
    }

    @Test
    void deleteEmployeeAsNonAdminRedirects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "EMPLOYEE");

        mockMvc.perform(post("/admin/employees/delete")
                        .session(session)
                        .param("employeeId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));

        verify(employeeService, never()).deleteEmployeeById(anyInt());
    }

    @Test
    void changePasswordSuccessful() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", employee);
        session.setAttribute("role", "EMPLOYEE");

        when(employeeService.isValidPassword("NewPass1!")).thenReturn(true);

        mockMvc.perform(post("/change-password")
                        .session(session)
                        .param("newPassword", "NewPass1!")
                        .param("confirmPassword", "NewPass1!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));

        verify(employeeService).updatePassword(employee);
    }

    @Test
    void changePasswordMismatch() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", employee);
        session.setAttribute("role", "EMPLOYEE");

        mockMvc.perform(post("/change-password")
                        .session(session)
                        .param("newPassword", "NewPass1!")
                        .param("confirmPassword", "WrongPass"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void changePasswordInvalidPassword() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", employee);
        session.setAttribute("role", "EMPLOYEE");

        when(employeeService.isValidPassword("simple")).thenReturn(false);

        mockMvc.perform(post("/change-password")
                        .session(session)
                        .param("newPassword", "simple")
                        .param("confirmPassword", "simple"))
                .andExpect(status().isOk())
                .andExpect(view().name("projects"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void changePasswordNoEmployeeInSession() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/change-password")
                        .session(session)
                        .param("newPassword", "NewPass1!")
                        .param("confirmPassword", "NewPass1!"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
