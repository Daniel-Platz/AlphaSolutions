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

import java.util.List;

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
        employee = new Employee(1, "testFirstname", "testLastname", "test@mail.com", Role.EMPLOYEE, "testingPassword");
    }

    @Test
    void homeRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void showLoginPageReturnsLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void loginSuccessRedirectsToDashboard() throws Exception {
        when(employeeService.findByEmailAndPassword("test@alphasolutions.dk", "password")).thenReturn(employee);

        mockMvc.perform(post("/login")
                        .param("emailPrefix", "test") // ✅ fixed
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"))
                .andExpect(request().sessionAttribute("employee", employee))
                .andExpect(request().sessionAttribute("employeeId", 1))
                .andExpect(request().sessionAttribute("role", "EMPLOYEE"));
    }

    @Test
    void loginFailureReturnsLoginViewWithError() throws Exception {
        when(employeeService.findByEmailAndPassword("test@alphasolutions.dk", "wrongpassword"))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/login")
                        .param("emailPrefix", "test") // ✅ fixed
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

    @Test
    void showEmployeeManagementAsAdminReturnsEmployeeManagementView() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "ADMIN");
        session.setAttribute("employee", employee);
        session.setAttribute("employeeId", employee.getEmployeeId());

        List<Employee> employees = List.of(employee);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/admin/employees").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("employee-management"))
                .andExpect(model().attribute("employees", employees));
    }

    @Test
    void showEmployeeManagementAsNonAdminRedirectsToProjects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "EMPLOYEE");
        session.setAttribute("employee", employee);
        session.setAttribute("employeeId", employee.getEmployeeId());

        mockMvc.perform(get("/admin/employees").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
    }

    @Test
    void addEmployeeAsAdminAddsEmployeeAndRedirects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "ADMIN");
        session.setAttribute("employee", employee);
        session.setAttribute("employeeId", employee.getEmployeeId());

        mockMvc.perform(post("/admin/employees/add")
                        .session(session)
                        .param("firstname", "Jane")
                        .param("lastname", "Doe")
                        .param("email", "jane.doe@mail.com")
                        .param("role", "EMPLOYEE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"))
                .andExpect(flash().attribute("newPassword", "velkommen123"));
    }

    @Test
    void addEmployeeAsNonAdminRedirectsToProjects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "EMPLOYEE");
        session.setAttribute("employee", employee);
        session.setAttribute("employeeId", employee.getEmployeeId());

        mockMvc.perform(post("/admin/employees/add")
                        .session(session)
                        .param("firstname", "Jane")
                        .param("lastname", "Doe")
                        .param("email", "jane.doe@mail.com")
                        .param("role", "EMPLOYEE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void deleteEmployeeAsAdminDeletesAndRedirects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "ADMIN");
        session.setAttribute("employee", employee);
        session.setAttribute("employeeId", employee.getEmployeeId());

        mockMvc.perform(post("/admin/employees/delete")
                        .session(session)
                        .param("employeeId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));
    }

    @Test
    void deleteEmployeeAsNonAdminRedirectsToProjects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "EMPLOYEE");
        session.setAttribute("employee", employee);
        session.setAttribute("employeeId", employee.getEmployeeId());

        mockMvc.perform(post("/admin/employees/delete")
                        .session(session)
                        .param("employeeId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void changePasswordSuccessRedirectsToDashboard() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", employee);
        session.setAttribute("role", "EMPLOYEE");

        when(employeeService.isValidPassword("NewPassword1")).thenReturn(true);

        mockMvc.perform(post("/change-password")
                        .session(session)
                        .param("newPassword", "NewPassword1")
                        .param("confirmPassword", "NewPassword1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void changePasswordMismatchReturnsProjectsWithError() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", employee);
        session.setAttribute("role", "EMPLOYEE");

        mockMvc.perform(post("/change-password")
                        .session(session)
                        .param("newPassword", "abc")
                        .param("confirmPassword", "def"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void changePasswordInvalidPatternReturnsProjectsWithError() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", employee);
        session.setAttribute("role", "EMPLOYEE");

        when(employeeService.isValidPassword("abc")).thenReturn(false);

        mockMvc.perform(post("/change-password")
                        .session(session)
                        .param("newPassword", "abc")
                        .param("confirmPassword", "abc"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void updateEmployeeAsAdminWithPasswordUpdatesAndRedirects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "ADMIN");

        mockMvc.perform(post("/admin/employees/update")
                        .session(session)
                        .param("employeeId", "1")
                        .param("firstname", "Updated")
                        .param("lastname", "User")
                        .param("password", "NewPassword1!")
                        .param("role", "EMPLOYEE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));
    }

    @Test
    void updateEmployeeAsAdminWithoutPasswordKeepsOldPassword() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "ADMIN");

        when(employeeService.getAllEmployees()).thenReturn(List.of(employee));

        mockMvc.perform(post("/admin/employees/update")
                        .session(session)
                        .param("employeeId", "1")
                        .param("firstname", "Updated")
                        .param("lastname", "User")
                        .param("role", "EMPLOYEE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/employees"));
    }

    @Test
    void updateEmployeeAsNonAdminRedirectsToProjects() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("role", "EMPLOYEE");

        mockMvc.perform(post("/admin/employees/update")
                        .session(session)
                        .param("employeeId", "1")
                        .param("firstname", "Updated")
                        .param("lastname", "User")
                        .param("role", "EMPLOYEE"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    void updatePasswordSuccessUpdatesAndShowsSuccessMessage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", employee);

        when(employeeService.isValidPassword("NewPassword1!")).thenReturn(true);

        mockMvc.perform(post("/employee-profile/change-password")
                        .session(session)
                        .param("currentPassword", "testingPassword")
                        .param("newPassword", "NewPassword1!")
                        .param("confirmPassword", "NewPassword1!"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-profile"))
                .andExpect(model().attributeExists("success"));
    }

    @Test
    void updatePasswordFailsWhenCurrentPasswordIsWrong() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", employee);

        mockMvc.perform(post("/employee-profile/change-password")
                        .session(session)
                        .param("currentPassword", "wrongPassword")
                        .param("newPassword", "NewPassword1!")
                        .param("confirmPassword", "NewPassword1!"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-profile"))
                .andExpect(model().attribute("error", "Nuværende kodeord er forkert."));
    }

    @Test
    void updatePasswordFailsWhenPasswordsDoNotMatch() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", employee);

        mockMvc.perform(post("/employee-profile/change-password")
                        .session(session)
                        .param("currentPassword", "testingPassword")
                        .param("newPassword", "NewPassword1!")
                        .param("confirmPassword", "DifferentPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-profile"))
                .andExpect(model().attribute("error", "Kodeordene matcher ikke."));
    }

    @Test
    void updatePasswordFailsWhenInvalidPattern() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("employee", employee);

        when(employeeService.isValidPassword("password")).thenReturn(false);

        mockMvc.perform(post("/employee-profile/change-password")
                        .session(session)
                        .param("currentPassword", "testingPassword")
                        .param("newPassword", "password")
                        .param("confirmPassword", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("user-profile"))
                .andExpect(model().attribute("error", "Kodeordet skal indeholde mindst ét stort bogstav, et tal og et specialtegn."));
    }
}