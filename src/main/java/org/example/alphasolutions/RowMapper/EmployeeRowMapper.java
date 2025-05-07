package org.example.alphasolutions.RowMapper;

import org.example.alphasolutions.enums.Role;
import org.example.alphasolutions.model.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();
        employee.setEmployeeId(rs.getInt("employee_id"));
        employee.setFirstname(rs.getString("firstname"));
        employee.setLastname(rs.getString("lastname"));
        employee.setEmail(rs.getString("email"));
        employee.setPassword(rs.getString("password"));

        String roleString = rs.getString("role");
        if (roleString != null) {
            employee.setRole(Role.valueOf(roleString.toUpperCase()));
        }

        return employee;
    }
}
