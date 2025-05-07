package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.Role;
import org.example.alphasolutions.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Sql(scripts = "classpath:h2init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class EmployeeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void resetAutoIncrement() {
        jdbcTemplate.execute("ALTER TABLE Employee ALTER COLUMN employee_id RESTART WITH 7");
    }

    @Test
    public void testSaveEmployee() {
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
    public void testDeleteEmployeeById() {
        employeeRepository.deleteEmployeeById(1);

        assertThrows(EmptyResultDataAccessException.class, () -> {
            employeeRepository.findByEmailAndPassword("admin@alphasolutions.com", "123456");
        });
    }

    @Test
    public void testShowAllEmployeesReturnsList() {
        List<Employee> all = employeeRepository.showAllEmployees();
        assertNotNull(all);
        assertFalse(all.isEmpty());
    }

    @Test
    public void testUpdatePassword() {
        String email = "admin@alphasolutions.com";
        String newPassword = "newSecurePass!";

        employeeRepository.updatePassword(email, newPassword);

        Employee updated = employeeRepository.findByEmailAndPassword(email, newPassword);

        assertEquals(email, updated.getEmail());
    }
}