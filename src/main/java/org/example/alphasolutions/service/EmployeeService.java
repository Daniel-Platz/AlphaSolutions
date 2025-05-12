package org.example.alphasolutions.service;

import org.example.alphasolutions.exception.InvalidCredentialsException;
import org.example.alphasolutions.model.Employee;
import org.example.alphasolutions.repository.EmployeeRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

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
        employee.setFirstname(capitalize(employee.getFirstname()));
        employee.setLastname(capitalize(employee.getLastname()));
        employee.setEmail(appendCompanyDomain(employee.getEmail()));
        employeeRepository.saveEmployee(employee);
    }

    public void deleteEmployeeById(int employeeId) {
        employeeRepository.deleteEmployeeById(employeeId);
    }

    private String capitalize(String name) {
        if (name == null || name.isBlank()) {
            return name;
        }
        name = name.trim().toLowerCase();
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private String appendCompanyDomain(String emailPrefix) {
        if (emailPrefix == null || emailPrefix.isBlank()) {
            return emailPrefix;
        }
        emailPrefix = emailPrefix.trim().toLowerCase();
        return emailPrefix + "@alphasolutions.dk";
    }

    public void updatePassword(Employee employee) {
        employeeRepository.updatePassword(employee.getEmail(), employee.getPassword());
    }

    public boolean isValidPassword(String password) {
        return password != null &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/].*");
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.showAllEmployees();
    }


    public List<Employee> gerAllManagers(){
        return employeeRepository.getAllManagers();
    }

}
