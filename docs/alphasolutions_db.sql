-- Create database
CREATE
DATABASE IF NOT EXISTS alphaSolutions_db;
USE
alphaSolutions_db;

-- Create Skill table
CREATE TABLE Skill
(
    skill_id   INT(10) PRIMARY KEY AUTO_INCREMENT,
    skill_name VARCHAR(255) NOT NULL
);

-- Create Employee table
CREATE TABLE Employee
(
    employee_id INT(10) PRIMARY KEY AUTO_INCREMENT,
    firstname   VARCHAR(255) NOT NULL,
    lastname    VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(255) NOT NULL
);

-- Create Project table
CREATE TABLE Project
(
    project_id              INT(10) PRIMARY KEY AUTO_INCREMENT,
    project_name            VARCHAR(255) NOT NULL,
    project_description     VARCHAR(255),
    project_start_date      DATE         NOT NULL,
    project_end_date        DATE,
    project_estimated_hours INT(10),
    project_status          VARCHAR(255) NOT NULL
);

-- Create Sub_project table
CREATE TABLE Sub_project
(
    sub_project_id              INT(10) PRIMARY KEY AUTO_INCREMENT,
    project_id                  INT(10) NOT NULL,
    sub_project_name            VARCHAR(255) NOT NULL,
    sub_project_description     VARCHAR(255),
    sub_project_start_date      DATE         NOT NULL,
    sub_project_end_date        DATE,
    sub_project_estimated_hours INT(10),
    sub_project_status          VARCHAR(255) NOT NULL,
    FOREIGN KEY (project_id) REFERENCES Project (project_id) ON DELETE CASCADE
);

-- Create Task table
CREATE TABLE Task
(
    task_id              INT(10) PRIMARY KEY AUTO_INCREMENT,
    sub_project_id       INT(10) NOT NULL,
    task_name            VARCHAR(255) NOT NULL,
    task_description     VARCHAR(255),
    task_start_date      DATE         NOT NULL,
    task_end_date        DATE,
    task_estimated_hours INT(10),
    task_status          VARCHAR(255) NOT NULL,
    FOREIGN KEY (sub_project_id) REFERENCES Sub_project (sub_project_id) ON DELETE CASCADE
);

-- Create junction table Employee_Skill (many-to-many)
CREATE TABLE Employee_Skill
(
    employee_id INT(10) NOT NULL,
    skill_id    INT(10) NOT NULL,
    PRIMARY KEY (employee_id, skill_id),
    FOREIGN KEY (employee_id) REFERENCES Employee (employee_id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES Skill (skill_id) ON DELETE CASCADE
);

-- Create junction table Project_Employee (many-to-many)
CREATE TABLE Project_Employee
(
    employee_id INT(10) NOT NULL,
    project_id  INT(10) NOT NULL,
    PRIMARY KEY (employee_id, project_id),
    FOREIGN KEY (employee_id) REFERENCES Employee (employee_id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES Project (project_id) ON DELETE CASCADE
);

-- Create junction table Employee_Task (many-to-many)
CREATE TABLE Employee_Task
(
    employee_id INT(10) NOT NULL,
    task_id     INT(10) NOT NULL,
    PRIMARY KEY (employee_id, task_id),
    FOREIGN KEY (employee_id) REFERENCES Employee (employee_id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES Task (task_id) ON DELETE CASCADE
);