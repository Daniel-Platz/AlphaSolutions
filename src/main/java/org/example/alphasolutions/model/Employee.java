package org.example.alphasolutions.model;

import org.example.alphasolutions.enums.Role;

public class Employee {
    private int employeeId;
    private String firstname;
    private String lastname;
    private String email;
    private Role role;

    public Employee(int employeeId, String firstname, String lastname, String email, Role employeerole) {
        this.employeeId = employeeId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = employeerole;
    }

    public Employee() {
    }


    //Getter & Setter

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    //Methods
    //TODO
    public void assignToProject(Project project) {

    }
    //TODO
    public void assignToTask(Task task) {

    }
    //TODO
    public void assignSkill(Skill skill) {

    }
    //TODO
    public void assignRole(Role role) {

    }
}