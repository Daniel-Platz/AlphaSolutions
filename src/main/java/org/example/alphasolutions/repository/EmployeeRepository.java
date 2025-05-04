package org.example.alphasolutions.repository;


import org.example.alphasolutions.model.Employee;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository

public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Employee findByEmailAndPassword (String email, String password) {
        String sql = "SELECT * FROM employee WHERE email = ? AND password = ?";
        return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Employee.class), email, password);
    }

    public void saveEmployee(Employee employee) {
        String sql = "INSERT INTO employee (firstname, lastname, email, role, password) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                employee.getFirstname(),
                employee.getLastname(),
                employee.getEmail(),
                employee.getRole().name(),
                employee.getPassword()
        );
    }

    public List<Employee> showAllEmployees() {
        String sql = "SELECT * FROM employee";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Employee.class));
    }

    public void updatePassword(String email, String newPassword) {
        String sql = "UPDATE employee SET password = ? WHERE email = ?";
        jdbcTemplate.update(sql, newPassword, email);
    }
}
