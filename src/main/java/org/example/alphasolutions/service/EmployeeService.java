package org.example.alphasolutions.service;

import org.example.alphasolutions.exception.InvalidCredentialsException;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.repository.EmployeeRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service

public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee findByEmailAndPassword(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw  new InvalidCredentialsException();
        }

        try {
            return employeeRepository.findByEmailAndPassword(email, password);
        } catch (EmptyResultDataAccessException e) {
            throw new InvalidCredentialsException();
        }
    }

    public void addEmployee(Employee employee) {
        employeeRepository.saveEmployee(employee);
    }

}
