package org.example.alphasolutions.enums;

public enum Role {
    ADMIN("The administrator of the system. Can do everything a manager can. Can also CRUD employees and change passwords"),
    PROJECT_MANAGER("Manager of projects. Can add employees to projects and tasks. Can also CRUD skills."),
    EMPLOYEE("A regular employee, can see their own projects.");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public static String getRoleByEmployeeRole(String employeeRole){
        for(Role role : Role.values()){
            if (role.name().equals(employeeRole)){
                return role.name();
            }
        }
        return "role doesn't exist";
    }

    public String getDescription() {
        return description;
    }
}