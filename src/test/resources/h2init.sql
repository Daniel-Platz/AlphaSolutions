-- Create Skill table
CREATE TABLE Skill
(
    skill_id   INT PRIMARY KEY AUTO_INCREMENT,
    skill_name VARCHAR(255) NOT NULL
);

-- Create Employee table
CREATE TABLE Employee
(
    employee_id INT PRIMARY KEY AUTO_INCREMENT,
    firstname   VARCHAR(255) NOT NULL,
    lastname    VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        VARCHAR(255) NOT NULL
);

-- Create Project table
CREATE TABLE Project
(
    project_id              INT PRIMARY KEY AUTO_INCREMENT,
    project_name            VARCHAR(255) NOT NULL,
    project_description     VARCHAR(255),
    project_start_date      DATE         NOT NULL,
    project_end_date        DATE,
    project_estimated_hours INT,
    project_status          VARCHAR(255) NOT NULL,
    manager_id              INT,
    FOREIGN KEY (manager_id) REFERENCES Employee (employee_id)
);

-- Create Sub_project table
CREATE TABLE Sub_project
(
    sub_project_id              INT PRIMARY KEY AUTO_INCREMENT,
    project_id                  INT          NOT NULL,
    sub_project_name            VARCHAR(255) NOT NULL,
    sub_project_description     VARCHAR(255),
    sub_project_start_date      DATE         NOT NULL,
    sub_project_end_date        DATE,
    sub_project_estimated_hours INT,
    sub_project_status          VARCHAR(255) NOT NULL,
    FOREIGN KEY (project_id) REFERENCES Project (project_id) ON DELETE CASCADE
);

-- Create Task table (with task_actual_hours column)
CREATE TABLE Task
(
    task_id              INT PRIMARY KEY AUTO_INCREMENT,
    sub_project_id       INT          NOT NULL,
    task_name            VARCHAR(255) NOT NULL,
    task_description     VARCHAR(255),
    task_start_date      DATE         NOT NULL,
    task_end_date        DATE,
    task_estimated_hours INT,
    task_actual_hours    INT DEFAULT 0,
    task_status          VARCHAR(255) NOT NULL,
    FOREIGN KEY (sub_project_id) REFERENCES Sub_project (sub_project_id) ON DELETE CASCADE
);

-- Create junction table Employee_Skill
CREATE TABLE Employee_Skill
(
    employee_id INT NOT NULL,
    skill_id    INT NOT NULL,
    PRIMARY KEY (employee_id, skill_id),
    FOREIGN KEY (employee_id) REFERENCES Employee (employee_id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES Skill (skill_id) ON DELETE CASCADE
);

-- Create junction table Project_Employee
CREATE TABLE Project_Employee
(
    employee_id INT NOT NULL,
    project_id  INT NOT NULL,
    PRIMARY KEY (employee_id, project_id),
    FOREIGN KEY (employee_id) REFERENCES Employee (employee_id) ON DELETE CASCADE,
    FOREIGN KEY (project_id) REFERENCES Project (project_id) ON DELETE CASCADE
);

-- Create junction table Employee_Task
CREATE TABLE Employee_Task
(
    employee_id INT NOT NULL,
    task_id     INT NOT NULL,
    PRIMARY KEY (employee_id, task_id),
    FOREIGN KEY (employee_id) REFERENCES Employee (employee_id) ON DELETE CASCADE,
    FOREIGN KEY (task_id) REFERENCES Task (task_id) ON DELETE CASCADE
);

-- Insert test data
INSERT INTO Skill (skill_id, skill_name)
VALUES (1, 'Java Development'),
       (2, 'Project Management'),
       (3, 'Scrum Master'),
       (4, 'Database Design'),
       (5, 'Frontend Development'),
       (6, 'DevOps');

INSERT INTO Employee (firstname, lastname, email, password, role)
VALUES ('John', 'Admin', 'admin@alphasolutions.com', '123456', 'ADMIN'),
       ('Sara', 'Manager', 'pm@alphasolutions.com', '123456', 'PROJECT_MANAGER'),
       ('Mike', 'Worker', 'employee@alphasolutions.com', '123456', 'EMPLOYEE'),
       ('Emma', 'Admin', 'admin2@alphasolutions.com', '123456', 'ADMIN'),
       ('David', 'Manager', 'pm2@alphasolutions.com', '123456', 'PROJECT_MANAGER'),
       ('Lisa', 'Developer', 'employee2@alphasolutions.com', '123456', 'EMPLOYEE');

INSERT INTO Employee_Skill (employee_id, skill_id)
VALUES (1, 1),
       (1, 4),
       (2, 2),
       (2, 3),
       (3, 1),
       (3, 5),
       (4, 2),
       (4, 4),
       (5, 2),
       (5, 6),
       (6, 1),
       (6, 5);

INSERT INTO Project (project_name, project_description, project_start_date, project_end_date,
                     project_estimated_hours, project_status, manager_id)
VALUES ('ERP System', 'Enterprise Resource Planning System Development', '2025-01-15', '2025-08-30', 2000, 'ACTIVE', 2),
       ('Mobile App', 'Customer Mobile Application Development', '2025-02-01', '2025-06-15', 1200, 'PLANNING', 5),
       ('Website Redesign', 'Corporate Website Redesign Project', '2024-11-01', '2025-01-15', 800, 'COMPLETED', 2),
       ('Archived Project', 'This project is archived', '2024-11-01', '2025-01-15', 400, 'ARCHIVED', 2);

INSERT INTO Project_Employee (employee_id, project_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 2),
       (5, 2),
       (6, 2),
       (2, 3),
       (3, 3),
       (6, 3);

INSERT INTO Sub_project (project_id, sub_project_name, sub_project_description, sub_project_start_date,
                         sub_project_end_date, sub_project_estimated_hours, sub_project_status)
VALUES (1, 'Database Design', 'Database schema design and implementation', '2025-01-15', '2025-03-15', 300, 'ACTIVE'),
       (1, 'Backend Development', 'API and business logic implementation', '2025-02-15', '2025-06-30', 800, 'PLANNING'),
       (1, 'Frontend Development', 'User interface development', '2025-04-01', '2025-07-31', 600, 'PLANNING'),
       (1, 'Testing', 'System testing and QA', '2025-07-01', '2025-08-15', 300, 'PLANNING'),
       (2, 'Requirements Analysis', 'Gathering and documenting requirements', '2025-02-01', '2025-02-28', 200, 'PLANNING'),
       (2, 'App Development', 'Implementing the mobile application', '2025-03-01', '2025-05-15', 800, 'PLANNING'),
       (2, 'Testing', 'QA and user acceptance testing', '2025-05-16', '2025-06-15', 200, 'PLANNING'),
       (3, 'Design', 'UI/UX design for website', '2024-11-01', '2024-11-30', 200, 'COMPLETED'),
       (3, 'Implementation', 'Front-end development', '2024-12-01', '2025-01-15', 600, 'COMPLETED');

INSERT INTO Task (sub_project_id, task_name, task_description, task_start_date, task_end_date,
                  task_estimated_hours, task_actual_hours, task_status)
VALUES
    -- SubProject 1 tasks (Database Design)
    (1, 'ER Diagram', 'Create entity-relationship diagrams', '2025-01-15', '2025-01-31', 80, 75, 'COMPLETED'),
    (1, 'Schema Creation', 'Define database schema', '2025-02-01', '2025-02-15', 100, 60, 'IN_PROGRESS'),
    (1, 'Data Migration Plan', 'Plan for migrating existing data', '2025-02-16', '2025-03-15', 120, 0, 'NOT_STARTED'),

    -- SubProject 2 tasks (Backend Development)
    (2, 'API Design', 'Design RESTful API endpoints', '2025-02-15', '2025-03-15', 200, 0, 'NOT_STARTED'),
    (2, 'Business Logic', 'Implement core business logic', '2025-03-16', '2025-05-15', 400, 0, 'NOT_STARTED'),
    (2, 'Integration', 'Integrate with external systems', '2025-05-16', '2025-06-30', 200, 0, 'NOT_STARTED'),

    -- SubProject 3 tasks (Frontend Development)
    (3, 'Component Design', 'Design reusable UI components', '2025-04-01', '2025-05-15', 200, 0, 'NOT_STARTED'),
    (3, 'UI Implementation', 'Implement user interfaces', '2025-05-16', '2025-07-15', 300, 0, 'NOT_STARTED'),
    (3, 'Responsive Design', 'Ensure responsive design for all devices', '2025-07-16', '2025-07-31', 100, 0, 'NOT_STARTED'),

    -- SubProject 8 tasks (Design)
    (8, 'Wireframes', 'Create wireframes for new website', '2024-11-01', '2024-11-15', 80, 85, 'COMPLETED'),
    (8, 'Visual Design', 'Create visual design elements', '2024-11-16', '2024-11-30', 120, 130, 'COMPLETED'),

    -- SubProject 9 tasks (Implementation)
    (9, 'HTML/CSS', 'Convert design to HTML/CSS', '2024-12-01', '2024-12-15', 200, 180, 'COMPLETED'),
    (9, 'JavaScript', 'Implement interactive features', '2024-12-16', '2025-01-05', 250, 270, 'COMPLETED'),
    (9, 'Testing', 'Cross-browser testing', '2025-01-06', '2025-01-15', 150, 140, 'COMPLETED');

INSERT INTO Employee_Task (employee_id, task_id)
VALUES (1, 1),
       (1, 2),
       (4, 3),
       (3, 4),
       (3, 5),
       (6, 6),
       (3, 7),
       (6, 8),
       (6, 9),
       (3, 10),
       (6, 11),
       (3, 12),
       (6, 13),
       (3, 14);