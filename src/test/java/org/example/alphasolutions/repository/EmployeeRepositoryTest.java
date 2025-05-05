package org.example.alphasolutions.repository;

import org.example.alphasolutions.enums.Role;
import org.example.alphasolutions.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

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
        String password ="123456";

        Employee employee = employeeRepository.findByEmailAndPassword(email,password);

        assertEquals(email, employee.getEmail());
        assertEquals("John", employee.getFirstname());
        assertEquals("Admin", employee.getLastname());
        assertEquals(Role.ADMIN, employee.getRole());
    }

}