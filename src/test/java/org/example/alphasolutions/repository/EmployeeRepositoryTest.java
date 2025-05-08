package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.Role;
import org.example.alphasolutions.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Sql(scripts = "classpath:h2init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void testFindByEmailAndPasswordSuccess() {
        String email = "admin@alphasolutions.com";
        String password = "123456";

        Employee employee = employeeRepository.findByEmailAndPassword(email, password);

        assertEquals(email, employee.getEmail());
        assertEquals("John", employee.getFirstname());
        assertEquals("Admin", employee.getLastname());
        assertEquals(Role.ADMIN, employee.getRole());
    }

    @Test
    public void testFindByEmailAndPasswordWrongPassword() {
        String email = "admin@alphasolutions.com";
        String password = "wrong password";

        assertThrows(EmptyResultDataAccessException.class, () -> employeeRepository.findByEmailAndPassword(email, password));
    }

    @Test
    public void testFindByEmailAndPasswordNonExistingUser() {
        String email = "nobody@alphasolutions.com";
        String password = "123456";

        assertThrows(EmptyResultDataAccessException.class, () -> employeeRepository.findByEmailAndPassword(email, password));
    }

    @Test
    public void saveEmployee() {
        Employee employee = new Employee();
        employee.setFirstname("Alice");
        employee.setLastname("Wonderland");
        employee.setEmail("alice@alphasolutions.com");
        employee.setRole(Role.EMPLOYEE);
        employee.setPassword("test123");

        employeeRepository.saveEmployee(employee);

        Employee fetched = employeeRepository.findByEmailAndPassword("alice@alphasolutions.com", "test123");

        assertNotNull(fetched);
        assertEquals("Alice", fetched.getFirstname());
        assertEquals("Wonderland", fetched.getLastname());
        assertEquals("alice@alphasolutions.com", fetched.getEmail());
        assertEquals(Role.EMPLOYEE, fetched.getRole());
        assertEquals("test123", fetched.getPassword());
    }

    @Test
    public void deleteEmployeeById() {
        employeeRepository.deleteEmployeeById(1);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            employeeRepository.findByEmailAndPassword("admin@alphasolutions.com", "123456");
        });
    }

    @Test
    public void showAllEmployeesReturnsList() {
        List<Employee> all = employeeRepository.showAllEmployees();
        assertNotNull(all);
        assertFalse(all.isEmpty());
    }

    @Test
    public void updatePassword() {
        String email = "admin@alphasolutions.com";
        String newPassword = "newSecurePass!";

        employeeRepository.updatePassword(email, newPassword);

        Employee updated = employeeRepository.findByEmailAndPassword(email, newPassword);

        assertEquals(email, updated.getEmail());
    }

    @Test
    public void updateEmployeeUpdatesCorrectly() {
        Employee employee = new Employee();
        employee.setFirstname("Bob");
        employee.setLastname("Builder");
        employee.setEmail("bob@alphasolutions.com");
        employee.setRole(Role.EMPLOYEE);
        employee.setPassword("build123");

        employeeRepository.saveEmployee(employee);

        Employee saved = employeeRepository.findByEmailAndPassword("bob@alphasolutions.com", "build123");

        saved.setFirstname("Robert");
        saved.setLastname("Fixer");
        saved.setRole(Role.ADMIN);
        saved.setPassword("newPassword123!");

        employeeRepository.updateEmployee(saved);

        Employee updated = employeeRepository.findByEmailAndPassword("bob@alphasolutions.com", "newPassword123!");
        assertEquals("Robert", updated.getFirstname());
        assertEquals("Fixer", updated.getLastname());
        assertEquals(Role.ADMIN, updated.getRole());
        assertEquals("newPassword123!", updated.getPassword());
    }
}