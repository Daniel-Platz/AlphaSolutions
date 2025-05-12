-- h2init.sql - Kombineret script til database initialisering for test

-- Ryd eksisterende tabeller










-- Create Skill table
DROP TABLE IF EXISTS Skill;
CREATE TABLE Skill (
    skill_id INT  PRIMARY KEY AUTO_INCREMENT,
    skill_name VARCHAR(255) NOT NULL
);


-- Create Employee table
DROP TABLE IF EXISTS Employee;
CREATE TABLE Employee (
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

--Insert test data Employee
INSERT INTO Employee (firstname, lastname, email, password, role) VALUES
    ('Mikkel', 'Rosenbek', 'MR@Test.dk', 'MR123', 'ADMIN'),
    ('Jens', 'Jensen', 'JJ@Test.dk', 'JJ123', 'PROJECT_MANAGER'),
    ('Hans', 'Hansen', 'HH@Test.dk', 'HH123', 'EMPLOYEE');



-- Create Project table
DROP TABLE IF EXISTS Project;
CREATE TABLE Project (
    project_id INT PRIMARY KEY AUTO_INCREMENT,
    project_name VARCHAR(255) NOT NULL,
    project_description VARCHAR(255),
    project_start_date DATE NOT NULL,
    project_end_date DATE,
    project_estimated_hours INT,
    project_status VARCHAR(255) NOT NULL
);

--Insert test data Project
INSERT INTO project (project_id, project_name, project_description, project_start_date, project_end_date, project_estimated_hours, project_status) VALUES
    (1, 'Test Project', 'A project for testing', '2025-01-01', '2025-12-31', 100, 'ACTIVE');



-- Create Sub_project table
DROP TABLE IF EXISTS sub_project;
CREATE TABLE Sub_project (
    sub_project_id INT PRIMARY KEY AUTO_INCREMENT,
    project_id INT NOT NULL,
    sub_project_name VARCHAR(255) NOT NULL,
    sub_project_description VARCHAR(255),
    sub_project_start_date DATE NOT NULL,
    sub_project_end_date DATE,
    sub_project_estimated_hours INT,
    sub_project_status VARCHAR(255) NOT NULL,
    FOREIGN KEY (project_id) REFERENCES Project(project_id) ON DELETE CASCADE
);

--Insert test data Sub_Project
INSERT INTO sub_project (sub_project_id, project_id,  sub_project_name, sub_project_description, sub_project_start_date, sub_project_end_date, sub_project_estimated_hours, sub_project_status) VALUES
    (1, 1, 'Test SubProject1', 'A subproject for testing', '2025-01-01', '2025-06-30', 50, 'ACTIVE'),
    (2, 1, 'Test SubProject2', 'A subproject for testing2', '2025-01-01', '2025-06-30', 50, 'PLANNING');



-- Create Task table
DROP TABLE IF EXISTS task;
CREATE TABLE Task (
    task_id INT PRIMARY KEY AUTO_INCREMENT,
    sub_project_id INT NOT NULL,
    task_name VARCHAR(255) NOT NULL,
    task_description VARCHAR(255),
    task_start_date DATE NOT NULL,
    task_end_date DATE,
    task_estimated_hours INT,
    task_status VARCHAR(255) NOT NULL,
    FOREIGN KEY (sub_project_id) REFERENCES Sub_project(sub_project_id) ON DELETE CASCADE
);

INSERT INTO task (sub_project_id, task_name, task_description, task_start_date, task_end_date, task_estimated_hours, task_status) VALUES
    (1, 'Existing Task 1', 'Description for task 1', '2025-01-01', '2025-01-10', 5, 'IN_PROGRESS'),
    (1, 'Existing Task 2', 'Description for task 2', '2025-01-05', '2025-01-15', 10, 'COMPLETED'),
    (2, 'Existing Task 3', 'Description for task 3', '2025-01-05', '2025-01-15', 10, 'DELAYED');


-- Create junction table Employee_Skill (many-to-many)
DROP TABLE IF EXISTS Employee_Skill;
CREATE TABLE Employee_Skill (
    employee_id INT NOT NULL,
    skill_id INT NOT NULL,
    PRIMARY KEY (employee_id, skill_id),
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES Skill(skill_id) ON DELETE CASCADE
);



-- Create junction table Project_Employee (many-to-many)
DROP TABLE IF EXISTS Project_Employee;
CREATE TABLE Project_Employee (
    employee_id INT NOT NULL,
    project_id INT NOT NULL,
    PRIMARY KEY (employee_id, project_id),
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES Project(project_id) ON DELETE CASCADE
);

INSERT INTO Project_Employee(employee_id, project_id) VALUES
    (1,1),
    (3,1);


-- Create junction table Employee_Task (many-to-many)
DROP TABLE IF EXISTS Employee_Task;
CREATE TABLE Employee_Task (
    employee_id INT NOT NULL,
    task_id INT NOT NULL,
    PRIMARY KEY (employee_id, task_id),
    FOREIGN KEY (employee_id) REFERENCES Employee(employee_id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES Task(task_id) ON DELETE CASCADE
);

INSERT INTO Employee_Task(employee_id, task_id) VALUES
    (2,1),
    (2,2),
    (3,2);
