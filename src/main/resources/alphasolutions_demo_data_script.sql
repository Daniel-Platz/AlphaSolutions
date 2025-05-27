-- ==========================================
-- AlphaSolutions Demo Data Script
-- ==========================================
-
-- Dette script opretter realistiske demo data til AlphaSolutions projektstyringssystem.
-- Scriptet kan køres på en tom database for at få et fuldt funktionelt demo-miljø.

-- INDHOLD:
-- • 10 medarbejdere (1 admin, 2 project managers, 7 employees)
-- • 5 forskellige projekter i forskellige stadier
-- • 12 delprojekter med realistiske arbejdsopgaver
-- • 22 opgaver med tidsregistrering og status
-- • Komplette relationer mellem medarbejdere, projekter og opgaver

-- LOGIN INFORMATION:
-- Alle brugere har password: "velkommen123"
-- Admin bruger: john.admin@company.com

-- BRUG:
-- For kun admin bruger: Kør kun Employee INSERT med admin data
-- For fuldt demo: Kør hele scriptet
-- ==========================================

-- Use the production database
USE alphaSolutions_prod;

-- Insert Employees with generic names
INSERT INTO Employee (firstname, lastname, email, password, role) VALUES
-- Admin
('John', 'Admin', 'john.admin@company.com', 'velkommen123', 'ADMIN'),

-- Project Managers
('Sarah', 'Manager', 'sarah.manager@company.com', 'velkommen123', 'PROJECT_MANAGER'),
('Mike', 'Johnson', 'mike.johnson@company.com', 'velkommen123', 'PROJECT_MANAGER'),

-- Regular Employees
('Alice', 'Smith', 'alice.smith@company.com', 'velkommen123', 'EMPLOYEE'),
('Bob', 'Wilson', 'bob.wilson@company.com', 'velkommen123', 'EMPLOYEE'),
('Carol', 'Davis', 'carol.davis@company.com', 'velkommen123', 'EMPLOYEE'),
('David', 'Brown', 'david.brown@company.com', 'velkommen123', 'EMPLOYEE'),
('Emma', 'Taylor', 'emma.taylor@company.com', 'velkommen123', 'EMPLOYEE'),
('Frank', 'Miller', 'frank.miller@company.com', 'velkommen123', 'EMPLOYEE'),
('Grace', 'Lee', 'grace.lee@company.com', 'velkommen123', 'EMPLOYEE');

-- Insert Projects
INSERT INTO Project (project_name, project_description, project_start_date, project_end_date, project_estimated_hours, project_status, manager_id) VALUES
                                                                                                                                                       ('Website Redesign', 'Complete overhaul of company website with modern design', '2024-01-15', '2024-06-30', 800, 'ACTIVE', 2),
                                                                                                                                                       ('Mobile App Development', 'Native mobile application for iOS and Android', '2024-02-01', '2024-08-31', 1200, 'ACTIVE', 2),
                                                                                                                                                       ('Database Migration', 'Migrate legacy system to cloud database', '2024-03-01', '2024-05-15', 400, 'COMPLETED', 3),
                                                                                                                                                       ('Marketing Campaign', 'Q2 digital marketing campaign launch', '2024-04-01', '2024-07-31', 300, 'PLANNING', 3),
                                                                                                                                                       ('Security Audit', 'Comprehensive security assessment and improvements', '2024-05-01', '2024-09-30', 600, 'ON_HOLD', 2);

-- Insert Sub-projects
INSERT INTO Sub_project (project_id, sub_project_name, sub_project_description, sub_project_start_date, sub_project_end_date, sub_project_estimated_hours, sub_project_status) VALUES
-- Website Redesign sub-projects
(1, 'UI/UX Design', 'Create wireframes and visual designs', '2024-01-15', '2024-03-15', 200, 'COMPLETED'),
(1, 'Frontend Development', 'Implement responsive frontend', '2024-03-01', '2024-05-31', 350, 'ACTIVE'),
(1, 'Backend Integration', 'API development and database integration', '2024-04-01', '2024-06-15', 250, 'PLANNING'),

-- Mobile App Development sub-projects
(2, 'iOS Development', 'Native iOS application development', '2024-02-01', '2024-07-31', 600, 'ACTIVE'),
(2, 'Android Development', 'Native Android application development', '2024-02-15', '2024-08-15', 600, 'ACTIVE'),

-- Database Migration sub-projects
(3, 'Data Analysis', 'Analyze existing database structure', '2024-03-01', '2024-03-31', 100, 'COMPLETED'),
(3, 'Migration Execution', 'Execute database migration', '2024-04-01', '2024-04-30', 200, 'COMPLETED'),
(3, 'Testing & Validation', 'Test migrated data integrity', '2024-05-01', '2024-05-15', 100, 'COMPLETED'),

-- Marketing Campaign sub-projects
(4, 'Content Creation', 'Create marketing materials and content', '2024-04-01', '2024-06-30', 150, 'PLANNING'),
(4, 'Digital Advertising', 'Set up and manage online ads', '2024-05-01', '2024-07-31', 150, 'PLANNING'),

-- Security Audit sub-projects
(5, 'Vulnerability Assessment', 'Identify security vulnerabilities', '2024-05-01', '2024-07-31', 300, 'ON_HOLD'),
(5, 'Security Implementation', 'Implement security improvements', '2024-08-01', '2024-09-30', 300, 'PLANNING');

-- Insert Tasks
INSERT INTO Task (sub_project_id, task_name, task_description, task_start_date, task_end_date, task_estimated_hours, task_actual_hours, task_status) VALUES
-- UI/UX Design tasks
(1, 'User Research', 'Conduct user interviews and surveys', '2024-01-15', '2024-02-01', 40, 45, 'COMPLETED'),
(1, 'Wireframing', 'Create wireframes for all pages', '2024-02-01', '2024-02-20', 60, 55, 'COMPLETED'),
(1, 'Visual Design', 'Design visual mockups', '2024-02-20', '2024-03-15', 100, 110, 'COMPLETED'),

-- Frontend Development tasks
(2, 'Setup Development Environment', 'Configure build tools and dependencies', '2024-03-01', '2024-03-05', 20, 18, 'COMPLETED'),
(2, 'Homepage Implementation', 'Code responsive homepage', '2024-03-05', '2024-03-25', 80, 75, 'COMPLETED'),
(2, 'Product Pages', 'Implement product catalog pages', '2024-03-25', '2024-04-15', 120, 0, 'IN_PROGRESS'),
(2, 'Contact Form', 'Create interactive contact form', '2024-04-15', '2024-05-01', 50, 0, 'NOT_STARTED'),
(2, 'Testing & Bug Fixes', 'Cross-browser testing and fixes', '2024-05-01', '2024-05-31', 80, 0, 'NOT_STARTED'),

-- Backend Integration tasks
(3, 'API Design', 'Design RESTful API structure', '2024-04-01', '2024-04-15', 40, 0, 'NOT_STARTED'),
(3, 'Database Schema', 'Update database schema', '2024-04-15', '2024-05-01', 60, 0, 'NOT_STARTED'),
(3, 'API Implementation', 'Implement backend endpoints', '2024-05-01', '2024-05-31', 100, 0, 'NOT_STARTED'),
(3, 'Integration Testing', 'Test frontend-backend integration', '2024-06-01', '2024-06-15', 50, 0, 'NOT_STARTED'),

-- iOS Development tasks
(4, 'Project Setup', 'Initialize iOS project structure', '2024-02-01', '2024-02-05', 20, 22, 'COMPLETED'),
(4, 'Authentication Module', 'Implement user login/signup', '2024-02-05', '2024-02-20', 80, 85, 'COMPLETED'),
(4, 'Main Navigation', 'Create app navigation structure', '2024-02-20', '2024-03-10', 60, 58, 'COMPLETED'),
(4, 'Core Features', 'Implement main app functionality', '2024-03-10', '2024-06-15', 300, 120, 'IN_PROGRESS'),
(4, 'App Store Submission', 'Prepare and submit to App Store', '2024-07-01', '2024-07-31', 40, 0, 'NOT_STARTED'),

-- Android Development tasks
(5, 'Project Setup', 'Initialize Android project structure', '2024-02-15', '2024-02-20', 20, 19, 'COMPLETED'),
(5, 'Authentication Module', 'Implement user login/signup', '2024-02-20', '2024-03-05', 80, 82, 'COMPLETED'),
(5, 'Main Navigation', 'Create app navigation structure', '2024-03-05', '2024-03-25', 60, 0, 'IN_PROGRESS'),
(5, 'Core Features', 'Implement main app functionality', '2024-03-25', '2024-07-15', 300, 0, 'NOT_STARTED'),
(5, 'Play Store Submission', 'Prepare and submit to Play Store', '2024-08-01', '2024-08-15', 40, 0, 'NOT_STARTED');

-- Insert Project-Employee relationships (many-to-many)
INSERT INTO Project_Employee (employee_id, project_id) VALUES
-- Website Redesign team
(2, 1), -- Sarah Manager
(4, 1), -- Alice Smith
(5, 1), -- Bob Wilson
(8, 1), -- Emma Taylor

-- Mobile App Development team
(2, 1), -- Sarah Manager
(6, 2), -- Carol Davis
(7, 2), -- David Brown
(9, 2), -- Frank Miller

-- Database Migration team
(3, 3), -- Mike Johnson
(10, 3), -- Grace Lee

-- Marketing Campaign team
(3, 4), -- Mike Johnson
(4, 4), -- Alice Smith
(6, 4), -- Carol Davis

-- Security Audit team
(2, 5), -- Sarah Manager
(7, 5), -- David Brown
(9, 5), -- Frank Miller
(10, 5); -- Grace Lee

-- Insert Employee-Task assignments (many-to-many)
INSERT INTO Employee_Task (employee_id, task_id) VALUES
-- UI/UX Design tasks (Alice Smith)
(4, 1), (4, 2), (4, 3),

-- Frontend Development tasks (Bob Wilson & Emma Taylor)
(5, 4), (5, 5), (8, 6), (8, 7), (5, 8),

-- Backend Integration tasks (Alice Smith)
(4, 9), (4, 10), (4, 11), (4, 12),

-- iOS Development tasks (Carol Davis)
(6, 13), (6, 14), (6, 15), (6, 16), (6, 17),

-- Android Development tasks (David Brown)
(7, 18), (7, 19), (7, 20), (7, 21), (7, 22);

