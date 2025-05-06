package org.example.alphasolutions.repository;


import org.example.alphasolutions.model.Employee;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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


    public List<Employee> getallManagers() {
        String sql ="SELECT * FROM employee WHERE role = 'PROJECT_MANAGER'";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Employee.class));
    }
}
