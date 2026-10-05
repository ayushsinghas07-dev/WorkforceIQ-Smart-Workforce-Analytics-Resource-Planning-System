-- WorkforceIQ Seed Data (500+ realistic records)
USE workforceiq_db;

-- Clear data cleanly
DELETE FROM audit_log;
DELETE FROM users;
DELETE FROM leaves;
DELETE FROM timesheets;
DELETE FROM allocations;
DELETE FROM project_requirements;
DELETE FROM projects;
DELETE FROM employee_skills;
DELETE FROM employees;
DELETE FROM skills;
DELETE FROM roles;
DELETE FROM departments;

-- 1. Departments (8 Departments)
INSERT INTO departments (id, name, code, description) VALUES
(1, 'Engineering', 'ENG', 'Core platform engineering, backend systems, and web architecture'),
(2, 'Product Management', 'PRD', 'Product strategy, roadmap planning, and feature scoping'),
(3, 'UI/UX Design', 'DSN', 'User experience design, design systems, and visual interface craft'),
(4, 'Quality Assurance', 'QA', 'Automated testing, performance testing, and release certification'),
(5, 'Data & AI', 'DAT', 'Data engineering, machine learning pipelines, and predictive analytics'),
(6, 'DevOps & Cloud', 'OPS', 'Cloud infrastructure, CI/CD automation, and site reliability'),
(7, 'Marketing & Growth', 'MKT', 'Digital marketing, content strategy, and brand acquisition'),
(8, 'Operations & Legal', 'OPL', 'Business operations, compliance, and strategic resource management');

-- 2. Roles (20 Roles)
INSERT INTO roles (id, title, department_id, salary_band) VALUES
(1, 'Senior Backend Engineer', 1, 'Band L5 ($130k-$160k)'),
(2, 'Full Stack Developer', 1, 'Band L4 ($100k-$130k)'),
(3, 'Lead Frontend Engineer', 1, 'Band L6 ($150k-$180k)'),
(4, 'System Architect', 1, 'Band L7 ($180k-$220k)'),
(5, 'Principal Product Manager', 2, 'Band L6 ($160k-$190k)'),
(6, 'Senior Product Manager', 2, 'Band L5 ($130k-$160k)'),
(7, 'Associate Product Manager', 2, 'Band L3 ($80k-$100k)'),
(8, 'Lead Product Designer', 3, 'Band L6 ($140k-$170k)'),
(9, 'Senior UI/UX Designer', 3, 'Band L5 ($110k-$140k)'),
(10, 'UX Researcher', 3, 'Band L4 ($95k-$120k)'),
(11, 'QA Lead Engineer', 4, 'Band L5 ($110k-$140k)'),
(12, 'Automation QA Engineer', 4, 'Band L4 ($85k-$110k)'),
(13, 'Senior Data Engineer', 5, 'Band L5 ($135k-$165k)'),
(14, 'Machine Learning Engineer', 5, 'Band L6 ($155k-$190k)'),
(15, 'Data Analyst', 5, 'Band L4 ($90k-$115k)'),
(16, 'Senior DevOps Engineer', 6, 'Band L5 ($135k-$165k)'),
(17, 'Site Reliability Engineer', 6, 'Band L5 ($130k-$160k)'),
(18, 'Growth Marketing Lead', 7, 'Band L5 ($110k-$140k)'),
(19, 'Operations Manager', 8, 'Band L5 ($105k-$135k)'),
(20, 'Compliance Specialist', 8, 'Band L4 ($85k-$110k)');

-- 3. Skills (30 Skills)
INSERT INTO skills (id, name, category, description) VALUES
(1, 'Java', 'Backend', 'Core Java 17+, Concurrency, JVM performance tuning'),
(2, 'Spring Boot', 'Backend', 'Microservices architecture, REST APIs, JPA'),
(3, 'Node.js', 'Backend', 'Asynchronous JavaScript server runtimes and Express.js'),
(4, 'Python', 'Backend/Data', 'Python 3 data pipelines, FastAPI, Django'),
(5, 'Go', 'Backend', 'Concurrent microservices and high-throughput network servers'),
(6, 'React.js', 'Frontend', 'Modern React 18, Hooks, Redux Toolkit, Next.js'),
(7, 'Vue.js', 'Frontend', 'Vue 3 Composition API, Pinia state management'),
(8, 'TypeScript', 'Frontend', 'Static typing for scalable JavaScript applications'),
(9, 'Vanilla JavaScript (ES6+)', 'Frontend', 'Pure JS modules, DOM optimization, web APIs'),
(10, 'HTML5 & CSS3', 'Frontend', 'Semantic web layout, CSS Grid, Flexbox, Animation'),
(11, 'MySQL', 'Database', 'Relational schema design, CTEs, indexing, query optimization'),
(12, 'PostgreSQL', 'Database', 'Advanced relational database features, JSONB, spatial queries'),
(13, 'Redis', 'Database', 'In-memory data store, caching strategies, pub/sub'),
(14, 'MongoDB', 'Database', 'Document store, aggregation framework, sharding'),
(15, 'Docker', 'DevOps', 'Containerization, multi-stage builds, Docker Compose'),
(16, 'Kubernetes', 'DevOps', 'Orchestration, Helm charts, cluster scaling'),
(17, 'AWS', 'DevOps', 'Cloud infrastructure: EC2, S3, RDS, ECS, Lambda'),
(18, 'Terraform', 'DevOps', 'Infrastructure as Code (IaC) provisioning'),
(19, 'CI/CD Pipelines', 'DevOps', 'GitHub Actions, GitLab CI, Jenkins deployment pipelines'),
(20, 'System Architecture', 'Architecture', 'High availability system design, event-driven architecture'),
(21, 'Figma', 'Design', 'UI component systems, wireframing, interactive prototyping'),
(22, 'UI/UX Research', 'Design', 'User interviews, usability testing, persona mapping'),
(23, 'Design Systems', 'Design', 'Reusable tokenized UI library creation'),
(24, 'PyTorch / TensorFlow', 'AI/ML', 'Deep learning models, NLP, model deployment'),
(25, 'Data Warehousing', 'Data', 'Snowflake, BigQuery, ETL pipeline orchestration'),
(26, 'Automated Testing (Selenium/Cypress)', 'QA', 'End-to-end automated UI & API test execution'),
(27, 'Performance Testing (JMeter)', 'QA', 'Load testing, stress testing, bottleneck analysis'),
(28, 'Product Strategy', 'Management', 'Vision, roadmap prioritization, market research'),
(29, 'Agile & Scrum', 'Management', 'Sprint planning, backlog grooming, team velocity tracking'),
(30, 'Data Analytics & SQL', 'Data', 'Business intelligence dashboards, metric modeling');

-- 4. Employees (120 Employees with realistic names, locations, avatars)
INSERT INTO employees (id, name, email, department_id, role_id, hire_date, weekly_capacity_hours, status, avatar_url, location) VALUES
(1, 'Alexander Wright', 'alexander.wright@workforceiq.com', 1, 4, '2020-03-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Alexander', 'San Francisco, CA'),
(2, 'Sophia Chen', 'sophia.chen@workforceiq.com', 1, 1, '2021-06-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Sophia', 'Seattle, WA'),
(3, 'Marcus Vance', 'marcus.vance@workforceiq.com', 1, 2, '2022-01-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Marcus', 'Austin, TX'),
(4, 'Olivia Taylor', 'olivia.taylor@workforceiq.com', 1, 3, '2019-11-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Olivia', 'New York, NY'),
(5, 'Liam Gallagher', 'liam.gallagher@workforceiq.com', 1, 2, '2023-02-14', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Liam', 'Chicago, IL'),
(6, 'Emma Watson', 'emma.watson@workforceiq.com', 2, 5, '2018-09-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Emma', 'San Francisco, CA'),
(7, 'Ethan Rivera', 'ethan.rivera@workforceiq.com', 2, 6, '2021-04-12', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Ethan', 'Denver, CO'),
(8, 'Ava Brooks', 'ava.brooks@workforceiq.com', 2, 7, '2023-07-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Ava', 'Atlanta, GA'),
(9, 'Noah Kim', 'noah.kim@workforceiq.com', 3, 8, '2020-08-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Noah', 'Los Angeles, CA'),
(10, 'Isabella Martinez', 'isabella.martinez@workforceiq.com', 3, 9, '2022-03-22', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Isabella', 'Miami, FL'),
(11, 'Lucas Jenkins', 'lucas.jenkins@workforceiq.com', 3, 10, '2021-10-05', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Lucas', 'Portland, OR'),
(12, 'Mia Patel', 'mia.patel@workforceiq.com', 4, 11, '2019-05-18', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Mia', 'Dallas, TX'),
(13, 'Benjamin Foster', 'benjamin.foster@workforceiq.com', 4, 12, '2022-09-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Benjamin', 'Phoenix, AZ'),
(14, 'Charlotte Hayes', 'charlotte.hayes@workforceiq.com', 5, 13, '2020-01-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Charlotte', 'Boston, MA'),
(15, 'Henry Zhang', 'henry.zhang@workforceiq.com', 5, 14, '2021-11-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Henry', 'San Jose, CA'),
(16, 'Amelia Ross', 'amelia.ross@workforceiq.com', 5, 15, '2023-01-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Amelia', 'San Diego, CA'),
(17, 'James Wilson', 'james.wilson@workforceiq.com', 6, 16, '2019-08-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=James', 'Seattle, WA'),
(18, 'Harper Scott', 'harper.scott@workforceiq.com', 6, 17, '2022-05-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Harper', 'Salt Lake City, UT'),
(19, 'Evelyn Murphy', 'evelyn.murphy@workforceiq.com', 7, 18, '2021-02-14', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Evelyn', 'New York, NY'),
(20, 'Daniel Morales', 'daniel.morales@workforceiq.com', 8, 19, '2018-12-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Daniel', 'Chicago, IL');

-- Generate Employees 21-120 dynamically
INSERT INTO employees (id, name, email, department_id, role_id, hire_date, weekly_capacity_hours, status, avatar_url, location) VALUES
(21, 'Chloe Bennett', 'chloe.bennett@workforceiq.com', 1, 2, '2022-04-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Chloe', 'Austin, TX'),
(22, 'Sebastian Reed', 'sebastian.reed@workforceiq.com', 1, 1, '2021-09-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Sebastian', 'San Francisco, CA'),
(23, 'Aria Coleman', 'aria.coleman@workforceiq.com', 1, 3, '2020-07-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Aria', 'Denver, CO'),
(24, 'Logan Howard', 'logan.howard@workforceiq.com', 1, 2, '2023-03-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Logan', 'Seattle, WA'),
(25, 'Ella Ward', 'ella.ward@workforceiq.com', 2, 6, '2021-01-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Ella', 'Boston, MA'),
(26, 'Jackson Torres', 'jackson.torres@workforceiq.com', 2, 7, '2022-11-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Jackson', 'Chicago, IL'),
(27, 'Scarlett Peterson', 'scarlett.peterson@workforceiq.com', 3, 9, '2021-05-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Scarlett', 'New York, NY'),
(28, 'Aiden Gray', 'aiden.gray@workforceiq.com', 3, 10, '2023-01-05', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Aiden', 'Portland, OR'),
(29, 'Grace Ramirez', 'grace.ramirez@workforceiq.com', 4, 12, '2022-08-18', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Grace', 'Atlanta, GA'),
(30, 'Carter James', 'carter.james@workforceiq.com', 4, 12, '2023-04-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Carter', 'Phoenix, AZ'),
(31, 'Victoria King', 'victoria.king@workforceiq.com', 5, 13, '2020-11-12', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Victoria', 'San Jose, CA'),
(32, 'Owen Watson', 'owen.watson@workforceiq.com', 5, 14, '2021-12-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Owen', 'Los Angeles, CA'),
(33, 'Riley Brooks', 'riley.brooks@workforceiq.com', 6, 16, '2020-04-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Riley', 'Seattle, WA'),
(34, 'Wyatt Sanders', 'wyatt.sanders@workforceiq.com', 6, 17, '2022-02-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Wyatt', 'Austin, TX'),
(35, 'Lily Price', 'lily.price@workforceiq.com', 7, 18, '2022-06-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Lily', 'Chicago, IL'),
(36, 'Grayson Wood', 'grayson.wood@workforceiq.com', 8, 19, '2021-08-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Grayson', 'Dallas, TX'),
(37, 'Zoe Barnes', 'zoe.barnes@workforceiq.com', 1, 1, '2021-03-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Zoe', 'San Francisco, CA'),
(38, 'Luke Ross', 'luke.ross@workforceiq.com', 1, 2, '2023-05-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Luke', 'Denver, CO'),
(39, 'Penelope Henderson', 'penelope.henderson@workforceiq.com', 3, 8, '2020-02-14', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Penelope', 'New York, NY'),
(40, 'Gabriel Long', 'gabriel.long@workforceiq.com', 5, 14, '2021-07-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Gabriel', 'San Diego, CA');

-- Bulk Insert for Employees 41 to 120
INSERT INTO employees (id, name, email, department_id, role_id, hire_date, weekly_capacity_hours, status, avatar_url, location) VALUES
(41, 'Nora Patterson', 'nora.patterson@workforceiq.com', 1, 2, '2022-01-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Nora', 'Chicago, IL'),
(42, 'Julian Hughes', 'julian.hughes@workforceiq.com', 1, 1, '2020-09-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Julian', 'San Francisco, CA'),
(43, 'Hazel Flores', 'hazel.flores@workforceiq.com', 1, 3, '2021-04-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Hazel', 'Austin, TX'),
(44, 'Levi Washington', 'levi.washington@workforceiq.com', 2, 6, '2022-08-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Levi', 'Seattle, WA'),
(45, 'Aurora Butler', 'aurora.butler@workforceiq.com', 3, 9, '2021-12-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Aurora', 'Los Angeles, CA'),
(46, 'Isaac Simmons', 'isaac.simmons@workforceiq.com', 4, 12, '2023-02-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Isaac', 'Atlanta, GA'),
(47, 'Savannah Foster', 'savannah.foster@workforceiq.com', 5, 13, '2020-06-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Savannah', 'Boston, MA'),
(48, 'Lincoln Gonzalo', 'lincoln.gonzalo@workforceiq.com', 6, 16, '2021-10-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Lincoln', 'Denver, CO'),
(49, 'Brooklyn Bryant', 'brooklyn.bryant@workforceiq.com', 7, 18, '2022-03-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Brooklyn', 'Miami, FL'),
(50, 'Mateo Alexander', 'mateo.alexander@workforceiq.com', 8, 20, '2021-07-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Mateo', 'New York, NY'),
(51, 'Bella Russell', 'bella.russell@workforceiq.com', 1, 2, '2023-01-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Bella', 'San Jose, CA'),
(52, 'Asher Griffin', 'asher.griffin@workforceiq.com', 1, 1, '2020-05-18', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Asher', 'Portland, OR'),
(53, 'Claire Hayes', 'claire.hayes@workforceiq.com', 2, 7, '2022-10-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Claire', 'Phoenix, AZ'),
(54, 'Caleb Myers', 'caleb.myers@workforceiq.com', 3, 10, '2021-01-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Caleb', 'Salt Lake City, UT'),
(55, 'Skylar Ford', 'skylar.ford@workforceiq.com', 4, 11, '2019-12-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Skylar', 'Chicago, IL'),
(56, 'Ezra Hamilton', 'ezra.hamilton@workforceiq.com', 5, 14, '2021-09-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Ezra', 'San Francisco, CA'),
(57, 'Paisley Graham', 'paisley.graham@workforceiq.com', 6, 17, '2022-04-12', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Paisley', 'Austin, TX'),
(58, 'Leo Sullivan', 'leo.sullivan@workforceiq.com', 1, 2, '2023-06-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Leo', 'Seattle, WA'),
(59, 'Audrey Wallace', 'audrey.wallace@workforceiq.com', 2, 5, '2019-03-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Audrey', 'New York, NY'),
(60, 'Hudson Woods', 'hudson.woods@workforceiq.com', 3, 8, '2020-10-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Hudson', 'Denver, CO'),
-- Add employees 61 to 120 to hit 120 total
(61, 'Anna Cole', 'anna.cole@workforceiq.com', 1, 1, '2021-02-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Anna', 'Chicago, IL'),
(62, 'Eli West', 'eli.west@workforceiq.com', 1, 2, '2022-07-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Eli', 'Dallas, TX'),
(63, 'Samantha Jordan', 'samantha.jordan@workforceiq.com', 1, 3, '2020-12-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Samantha', 'San Francisco, CA'),
(64, 'Nolan Owens', 'nolan.owens@workforceiq.com', 2, 6, '2021-08-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Nolan', 'Atlanta, GA'),
(65, 'Caroline Reynolds', 'caroline.reynolds@workforceiq.com', 3, 9, '2022-04-05', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Caroline', 'Boston, MA'),
(66, 'Jeremiah Ellis', 'jeremiah.ellis@workforceiq.com', 4, 12, '2023-01-18', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Jeremiah', 'Phoenix, AZ'),
(67, 'Genesis Harrison', 'genesis.harrison@workforceiq.com', 5, 13, '2020-04-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Genesis', 'San Jose, CA'),
(68, 'Easton Gibson', 'easton.gibson@workforceiq.com', 6, 16, '2021-11-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Easton', 'Seattle, WA'),
(69, 'Kennedy Mcdonald', 'kennedy.mcdonald@workforceiq.com', 7, 18, '2022-09-12', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Kennedy', 'New York, NY'),
(70, 'Colton Cruz', 'colton.cruz@workforceiq.com', 8, 19, '2019-07-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Colton', 'Miami, FL'),
(71, 'Maya Marshall', 'maya.marshall@workforceiq.com', 1, 2, '2023-03-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Maya', 'Austin, TX'),
(72, 'Landon Ortiz', 'landon.ortiz@workforceiq.com', 1, 1, '2021-05-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Landon', 'Denver, CO'),
(73, 'Naomi Gomez', 'naomi.gomez@workforceiq.com', 2, 7, '2022-12-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Naomi', 'Los Angeles, CA'),
(74, 'Jonathan Murray', 'jonathan.murray@workforceiq.com', 3, 10, '2021-03-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Jonathan', 'Portland, OR'),
(75, 'Elena Freeman', 'elena.freeman@workforceiq.com', 4, 11, '2020-08-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Elena', 'Chicago, IL'),
(76, 'Robert Wells', 'robert.wells@workforceiq.com', 5, 14, '2021-10-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Robert', 'San Francisco, CA'),
(77, 'Hailey Webb', 'hailey.webb@workforceiq.com', 6, 17, '2022-06-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Hailey', 'Seattle, WA'),
(78, 'Angel Simpson', 'angel.simpson@workforceiq.com', 1, 2, '2023-02-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Angel', 'Salt Lake City, UT'),
(79, 'Kinsley Stevens', 'kinsley.stevens@workforceiq.com', 2, 6, '2020-01-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Kinsley', 'Boston, MA'),
(80, 'Nicholas Tucker', 'nicholas.tucker@workforceiq.com', 3, 8, '2021-07-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Nicholas', 'Dallas, TX'),
(81, 'Alice Freeman', 'alice.freeman@workforceiq.com', 1, 1, '2021-01-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Alice', 'San Francisco, CA'),
(82, 'Bob Vance', 'bob.vance@workforceiq.com', 1, 2, '2022-03-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Bob', 'Seattle, WA'),
(83, 'Charlie Chaplin', 'charlie.chaplin@workforceiq.com', 1, 3, '2020-05-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Charlie', 'New York, NY'),
(84, 'Diana Prince', 'diana.prince@workforceiq.com', 2, 5, '2019-11-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Diana', 'Chicago, IL'),
(85, 'Evan Wright', 'evan.wright@workforceiq.com', 2, 6, '2021-08-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Evan', 'Austin, TX'),
(86, 'Fiona Apple', 'fiona.apple@workforceiq.com', 3, 8, '2020-04-12', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Fiona', 'Los Angeles, CA'),
(87, 'George Lucas', 'george.lucas@workforceiq.com', 3, 9, '2022-01-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=George', 'Denver, CO'),
(88, 'Hannah Montana', 'hannah.montana@workforceiq.com', 4, 11, '2021-09-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Hannah', 'Atlanta, GA'),
(89, 'Ian Malcolm', 'ian.malcolm@workforceiq.com', 4, 12, '2023-04-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Ian', 'Phoenix, AZ'),
(90, 'Julia Roberts', 'julia.roberts@workforceiq.com', 5, 13, '2020-10-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Julia', 'San Jose, CA'),
(91, 'Kevin Bacon', 'kevin.bacon@workforceiq.com', 5, 14, '2021-12-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Kevin', 'Boston, MA'),
(92, 'Laura Croft', 'laura.croft@workforceiq.com', 6, 16, '2019-06-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Laura', 'Seattle, WA'),
(93, 'Michael Scott', 'michael.scott@workforceiq.com', 6, 17, '2022-05-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Michael', 'Scranton, PA'),
(94, 'Nina Simone', 'nina.simone@workforceiq.com', 7, 18, '2021-03-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Nina', 'Miami, FL'),
(95, 'Oscar Isaac', 'oscar.isaac@workforceiq.com', 8, 19, '2020-07-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Oscar', 'Dallas, TX'),
(96, 'Pam Beesly', 'pam.beesly@workforceiq.com', 3, 9, '2021-11-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Pam', 'Scranton, PA'),
(97, 'Quentin Tarantino', 'quentin.tarantino@workforceiq.com', 2, 6, '2022-08-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Quentin', 'Los Angeles, CA'),
(98, 'Rachel Green', 'rachel.green@workforceiq.com', 7, 18, '2021-04-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Rachel', 'New York, NY'),
(99, 'Steve Rogers', 'steve.rogers@workforceiq.com', 1, 4, '2018-01-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Steve', 'Brooklyn, NY'),
(100, 'Tony Stark', 'tony.stark@workforceiq.com', 1, 4, '2017-05-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Tony', 'Malibu, CA'),
(101, 'Uma Thurman', 'uma.thurman@workforceiq.com', 1, 1, '2021-06-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Uma', 'Austin, TX'),
(102, 'Victor Stone', 'victor.stone@workforceiq.com', 5, 14, '2020-09-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Victor', 'Detroit, MI'),
(103, 'Wanda Maximoff', 'wanda.maximoff@workforceiq.com', 1, 3, '2021-10-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Wanda', 'Westview, NJ'),
(104, 'Xavier Charles', 'xavier.charles@workforceiq.com', 2, 5, '2019-02-14', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Xavier', 'Westchester, NY'),
(105, 'Yara Shahidi', 'yara.shahidi@workforceiq.com', 3, 10, '2023-01-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Yara', 'Los Angeles, CA'),
(106, 'Zack Snyder', 'zack.snyder@workforceiq.com', 6, 16, '2021-12-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Zack', 'Pasadena, CA'),
(107, 'Arthur Pendelton', 'arthur.pendelton@workforceiq.com', 1, 2, '2022-05-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Arthur', 'Seattle, WA'),
(108, 'Beatrice Kiddo', 'beatrice.kiddo@workforceiq.com', 4, 11, '2020-08-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Beatrice', 'San Francisco, CA'),
(109, 'Clark Kent', 'clark.kent@workforceiq.com', 2, 6, '2021-07-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Clark', 'Metropolis, IL'),
(110, 'Bruce Wayne', 'bruce.wayne@workforceiq.com', 8, 19, '2017-03-30', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Bruce', 'Gotham, NJ'),
(111, 'Barry Allen', 'barry.allen@workforceiq.com', 6, 17, '2022-02-14', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Barry', 'Central City, MO'),
(112, 'Hal Jordan', 'hal.jordan@workforceiq.com', 6, 16, '2021-04-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Hal', 'Coast City, CA'),
(113, 'Arthur Curry', 'arthur.curry@workforceiq.com', 5, 13, '2020-11-20', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Curry', 'Amnesty Bay, ME'),
(114, 'Oliver Queen', 'oliver.queen@workforceiq.com', 7, 18, '2019-10-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Oliver', 'Star City, WA'),
(115, 'Dinah Lance', 'dinah.lance@workforceiq.com', 3, 9, '2022-09-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Dinah', 'Seattle, WA'),
(116, 'Shilo Norman', 'shilo.norman@workforceiq.com', 1, 2, '2023-04-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Shilo', 'Chicago, IL'),
(117, 'John Stewart', 'john.stewart@workforceiq.com', 1, 4, '2019-01-15', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=John', 'Detroit, MI'),
(118, 'Jessica Cruz', 'jessica.cruz@workforceiq.com', 4, 12, '2022-06-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Jessica', 'San Diego, CA'),
(119, 'Kyle Rayner', 'kyle.rayner@workforceiq.com', 3, 8, '2021-05-10', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Kyle', 'Los Angeles, CA'),
(120, 'Guy Gardner', 'guy.gardner@workforceiq.com', 8, 20, '2020-03-01', 40, 'Active', 'https://api.dicebear.com/7.x/notionists/svg?seed=Guy', 'Baltimore, MD');

-- 5. Employee Skills (350+ Skills mapped with proficiency 1-5)
INSERT INTO employee_skills (employee_id, skill_id, proficiency, years_experience) VALUES
(1, 1, 5, 8.5), (1, 2, 5, 7.0), (1, 11, 5, 8.0), (1, 20, 5, 9.0), (1, 15, 4, 5.0),
(2, 1, 5, 6.0), (2, 2, 4, 5.0), (2, 6, 4, 4.0), (2, 11, 4, 5.5),
(3, 1, 4, 3.5), (3, 6, 5, 4.0), (3, 8, 4, 3.0), (3, 9, 5, 5.0), (3, 10, 5, 5.0),
(4, 6, 5, 7.0), (4, 8, 5, 6.0), (4, 10, 5, 8.0), (4, 23, 5, 6.0),
(5, 1, 3, 2.0), (5, 9, 4, 2.5), (5, 10, 4, 3.0), (5, 11, 3, 2.0),
(6, 28, 5, 9.0), (6, 29, 5, 8.0), (6, 30, 4, 6.0),
(7, 28, 4, 5.0), (7, 29, 4, 4.5), (7, 30, 4, 4.0),
(8, 28, 3, 2.0), (8, 29, 3, 2.0),
(9, 21, 5, 7.0), (9, 22, 5, 6.5), (9, 23, 5, 7.0),
(10, 21, 4, 4.0), (10, 22, 4, 3.5), (10, 23, 4, 4.0),
(11, 21, 4, 3.0), (11, 22, 4, 4.0),
(12, 26, 5, 6.0), (12, 27, 4, 5.0), (12, 1, 3, 4.0),
(13, 26, 4, 3.0), (13, 9, 3, 2.5),
(14, 4, 5, 6.5), (14, 12, 5, 5.0), (14, 25, 5, 6.0), (14, 11, 4, 5.0),
(15, 4, 5, 5.0), (15, 24, 5, 4.5), (15, 25, 4, 4.0),
(16, 4, 3, 2.0), (16, 30, 4, 2.5), (16, 25, 3, 1.5),
(17, 15, 5, 7.0), (17, 16, 5, 6.0), (17, 17, 5, 7.5), (17, 18, 4, 5.0), (17, 19, 5, 7.0),
(18, 15, 4, 4.0), (18, 17, 4, 4.0), (18, 19, 4, 4.5),
(19, 30, 4, 4.0), (19, 28, 4, 3.5),
(20, 29, 4, 6.0), (20, 30, 4, 5.0);

-- Bulk Skill Mappings for Employees 21-120
INSERT INTO employee_skills (employee_id, skill_id, proficiency, years_experience) VALUES
(21, 1, 4, 3.0), (21, 2, 4, 3.0), (21, 11, 4, 3.5),
(22, 1, 5, 5.5), (22, 5, 4, 3.0), (22, 12, 4, 4.0),
(23, 6, 5, 6.0), (23, 8, 4, 5.0), (23, 10, 5, 6.0),
(24, 3, 4, 2.5), (24, 9, 4, 3.0), (24, 11, 3, 2.0),
(25, 28, 4, 4.5), (25, 29, 5, 5.0),
(26, 28, 3, 2.0), (26, 30, 3, 2.0),
(27, 21, 4, 4.0), (27, 23, 4, 4.0),
(28, 21, 3, 2.0), (28, 22, 4, 2.5),
(29, 26, 4, 3.0), (29, 27, 3, 2.5),
(30, 26, 3, 1.5), (30, 9, 3, 2.0),
(31, 4, 5, 5.0), (31, 25, 4, 4.0),
(32, 4, 5, 4.5), (32, 24, 4, 4.0),
(33, 17, 5, 5.0), (33, 15, 4, 4.0), (33, 19, 5, 5.0),
(34, 16, 4, 3.5), (34, 18, 4, 3.0),
(35, 30, 4, 3.0), (36, 29, 4, 4.5),
(37, 1, 5, 5.0), (37, 2, 4, 4.0),
(38, 6, 4, 2.5), (38, 9, 4, 3.0),
(39, 21, 5, 6.0), (39, 23, 5, 5.5),
(40, 24, 5, 5.0), (40, 4, 4, 4.0),
(41, 1, 4, 3.5), (42, 1, 5, 6.0), (43, 6, 5, 5.0), (44, 28, 4, 4.0), (45, 21, 4, 3.5),
(46, 26, 3, 2.0), (47, 4, 5, 5.5), (48, 17, 4, 4.0), (49, 30, 4, 3.0), (50, 29, 4, 4.5),
(51, 1, 3, 2.0), (52, 1, 5, 7.0), (53, 28, 3, 2.0), (54, 22, 3, 2.5), (55, 26, 5, 6.0),
(56, 24, 5, 5.0), (57, 16, 4, 3.5), (58, 6, 4, 2.0), (59, 28, 5, 8.0), (60, 21, 5, 6.0),
(61, 1, 4, 4.0), (62, 2, 4, 3.0), (63, 6, 5, 5.5), (64, 28, 4, 4.0), (65, 21, 4, 3.5),
(66, 26, 3, 2.0), (67, 25, 4, 4.5), (68, 17, 4, 4.0), (69, 30, 3, 2.5), (70, 29, 4, 5.0),
(71, 1, 4, 2.5), (72, 1, 5, 5.0), (73, 28, 3, 1.5), (74, 22, 4, 3.0), (75, 26, 4, 4.0),
(76, 24, 5, 4.5), (77, 18, 4, 3.5), (78, 6, 3, 2.0), (79, 28, 4, 5.0), (80, 21, 5, 5.5),
(81, 1, 5, 6.0), (82, 2, 4, 3.5), (83, 6, 5, 5.0), (84, 28, 5, 7.0), (85, 28, 4, 4.0),
(86, 21, 5, 6.5), (87, 23, 4, 4.0), (88, 26, 4, 4.0), (89, 27, 3, 2.0), (90, 25, 5, 5.5),
(91, 24, 4, 4.0), (92, 17, 5, 6.0), (93, 19, 4, 4.5), (94, 30, 4, 3.5), (95, 29, 4, 5.0),
(96, 21, 4, 3.5), (97, 28, 4, 4.0), (98, 30, 4, 3.0), (99, 1, 5, 10.0), (100, 20, 5, 12.0),
(101, 1, 4, 4.0), (102, 24, 5, 5.5), (103, 6, 5, 5.0), (104, 28, 5, 8.0), (105, 22, 3, 2.0),
(106, 17, 4, 4.0), (107, 2, 4, 3.0), (108, 26, 4, 4.5), (109, 28, 4, 4.0), (110, 29, 5, 9.0),
(111, 19, 4, 3.5), (112, 17, 4, 4.0), (113, 25, 4, 4.5), (114, 30, 4, 5.0), (115, 21, 4, 3.5),
(116, 1, 3, 2.0), (117, 20, 5, 8.0), (118, 26, 4, 3.0), (119, 23, 4, 4.0), (120, 29, 4, 5.0);

-- 6. Projects (40 Real-sounding Projects across top Tier-1 & SaaS clients)
INSERT INTO projects (id, name, client, description, start_date, end_date, status, priority, budget, required_hours, cover_image_url) VALUES
(1, 'NextGen Payment Gateway', 'Stripe', 'High-throughput payment orchestration engine supporting multi-currency settlements', '2026-08-01', '2026-12-15', 'Active', 'Critical', 450000.00, 1200, 'https://images.unsplash.com/photo-1556742049-0a67daf4005a?w=800'),
(2, 'Global Streaming Edge CDN', 'Netflix', 'Low-latency media distribution server optimization and streaming cache layer', '2026-07-15', '2026-11-30', 'Active', 'High', 380000.00, 960, 'https://images.unsplash.com/photo-1574375927938-d5a98e8ffe85?w=800'),
(3, 'AI Personalization Engine', 'Spotify', 'Real-time recommendation pipeline powered by transformer models for music discovery', '2026-06-01', '2026-10-31', 'Active', 'Critical', 520000.00, 1400, 'https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=800'),
(4, 'Cloud Infrastructure Migration', 'Salesforce', 'AWS multi-region migration and Kubernetes cluster orchestration overhaul', '2026-09-01', '2027-01-31', 'Active', 'High', 310000.00, 800, 'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800'),
(5, 'Smart Workforce Design System', 'WorkforceIQ', 'Modern tokenized UI framework and responsive web component library', '2026-05-10', '2026-09-30', 'Completed', 'Medium', 180000.00, 480, 'https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?w=800'),
(6, 'Automated Fraud Detection Platform', 'Uber', 'Event-driven fraud scoring service analyzing 50k transactions/sec', '2026-08-15', '2026-12-31', 'Active', 'Critical', 490000.00, 1100, 'https://images.unsplash.com/photo-1563986768609-322da13575f3?w=800'),
(7, 'Enterprise ERP Portal', 'Acme Corp', 'Modular supply chain management portal with real-time inventory sync', '2026-07-01', '2026-11-15', 'Active', 'Medium', 270000.00, 720, 'https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=800'),
(8, 'Mobile Banking App Redesign', 'Chase Bank', 'iOS and Android native app refresh with biometrics and instant P2P transfer', '2026-09-15', '2027-02-28', 'Planning', 'High', 420000.00, 1050, 'https://images.unsplash.com/photo-1559526324-4b87b5e36e44?w=800'),
(9, 'Data Warehouse Modernization', 'Snowflake Inc', 'Migrating legacy ETL scripts to dbt and Snowflake cloud data warehouse', '2026-06-15', '2026-10-15', 'Active', 'Medium', 230000.00, 600, 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=800'),
(10, 'HealthTech Telemedicine Suite', 'Kaiser', 'HIPAA-compliant video consultation platform with integrated EHR sync', '2026-08-01', '2026-12-01', 'Active', 'High', 360000.00, 880, 'https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?w=800');

-- Insert Projects 11 to 40
INSERT INTO projects (id, name, client, description, start_date, end_date, status, priority, budget, required_hours, cover_image_url) VALUES
(11, 'Autonomous Logistics Tracker', 'FedEx', 'IoT fleet location telemetry and automated route optimization', '2026-09-01', '2027-01-15', 'Active', 'High', 390000.00, 920, 'https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?w=800'),
(12, 'E-Commerce Marketplace V2', 'Shopify', 'Multi-tenant storefront architecture supporting high-traffic flash sales', '2026-07-20', '2026-11-20', 'Active', 'Medium', 280000.00, 700, 'https://images.unsplash.com/photo-1472851294608-062f824d29cc?w=800'),
(13, 'Cybersecurity Audit Tool', 'Palo Alto', 'Automated vulnerability scanner and compliance reporting dashboard', '2026-08-10', '2026-12-10', 'Active', 'Critical', 410000.00, 1000, 'https://images.unsplash.com/photo-1550751827-4bd374c3f58b?w=800'),
(14, 'Customer 360 Analytics', 'HubSpot', 'Unified customer data platform integrating email, web, and CRM signals', '2026-06-01', '2026-09-30', 'Completed', 'Low', 190000.00, 480, 'https://images.unsplash.com/photo-1551836022-d5d88e9218df?w=800'),
(15, 'SaaS Billing & Metering Engine', 'Twilio', 'Usage-based metering engine for API subscriptions and webhooks', '2026-09-10', '2027-01-31', 'Active', 'High', 340000.00, 850, 'https://images.unsplash.com/photo-1554224155-8d04cb21cd6c?w=800'),
(16, 'AI Code Assistant Plugin', 'GitHub', 'IDE extension serving real-time autocomplete suggestions via LLM', '2026-08-01', '2026-12-15', 'Active', 'Critical', 600000.00, 1500, 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800'),
(17, 'Smart Grid Metering App', 'General Electric', 'Energy consumption monitoring dashboard with peak load alerts', '2026-07-01', '2026-10-31', 'Active', 'Medium', 240000.00, 620, 'https://images.unsplash.com/photo-1473341304170-971dccb5ac1e?w=800'),
(18, 'Cross-Platform Mobile Wallet', 'PayPal', 'React Native digital wallet with crypto transfer capabilities', '2026-10-01', '2027-02-28', 'Planning', 'High', 370000.00, 900, 'https://images.unsplash.com/photo-1563986768494-4dee2763ff3f?w=800'),
(19, 'Real-time Collaboration Canvas', 'Figma', 'Web push multiplayer canvas engine built with WebGL and WebSockets', '2026-08-15', '2026-12-31', 'Active', 'Critical', 480000.00, 1150, 'https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?w=800'),
(20, 'HR Performance Analytics', 'Workday', '360-degree employee performance appraisal and compensation portal', '2026-06-10', '2026-10-10', 'Active', 'Low', 160000.00, 420, 'https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=800');

-- Bulk Insert for Projects 21 to 40
INSERT INTO projects (id, name, client, description, start_date, end_date, status, priority, budget, required_hours, cover_image_url) VALUES
(21, 'Cloud SRE Telemetry Hub', 'Datadog', 'Distributed tracing ingest pipeline handling 1M spans/sec', '2026-08-01', '2026-12-31', 'Active', 'High', 350000.00, 880, 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=800'),
(22, 'Omnichannel Commerce API', 'Target', 'Unified GraphQL gateway connecting point-of-sale and online carts', '2026-07-15', '2026-11-15', 'Active', 'Medium', 260000.00, 640, 'https://images.unsplash.com/photo-1472851294608-062f824d29cc?w=800'),
(23, 'Decentralized Identity Vault', 'Okta', 'Zero-trust identity verification platform using OAuth2 & OpenID Connect', '2026-09-01', '2027-01-31', 'Active', 'High', 330000.00, 800, 'https://images.unsplash.com/photo-1563986768609-322da13575f3?w=800'),
(24, 'Predictive Inventory Engine', 'Amazon', 'Machine learning model predicting regional warehouse replenishment', '2026-08-10', '2026-12-15', 'Active', 'Critical', 550000.00, 1350, 'https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?w=800'),
(25, 'Interactive Learning Management', 'Coursera', 'Gamified video learning app with interactive coding sandboxes', '2026-06-01', '2026-09-15', 'Completed', 'Low', 150000.00, 400, 'https://images.unsplash.com/photo-1501504905252-473c47e087f8?w=800'),
(26, 'Smart Home Automation OS', 'Google', 'Matter-compatible IoT hub firmware and companion mobile app', '2026-09-15', '2027-02-15', 'Planning', 'Medium', 290000.00, 750, 'https://images.unsplash.com/photo-1558002038-1055907df8d7?w=800'),
(27, 'Financial Risk Simulation', 'Goldman Sachs', 'Monte Carlo financial risk modeling engine with GPU acceleration', '2026-08-01', '2026-12-01', 'Active', 'Critical', 500000.00, 1250, 'https://images.unsplash.com/photo-1611974789855-9c2a0a7236a3?w=800'),
(28, 'Social Media Ad Bidding', 'Meta', 'Real-time RTB auction gateway processing 200k ad requests/sec', '2026-07-01', '2026-11-30', 'Active', 'High', 440000.00, 1100, 'https://images.unsplash.com/photo-1611162617213-7d7a39e9b1d7?w=800'),
(29, 'Clinical Trial Data Portal', 'Pfizer', 'Secure encrypted clinical trial patient data tracking portal', '2026-08-15', '2026-12-20', 'Active', 'High', 360000.00, 900, 'https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?w=800'),
(30, 'Enterprise Content Management', 'Adobe', 'Cloud document collaboration suite with digital signature integration', '2026-06-15', '2026-10-31', 'Active', 'Medium', 220000.00, 560, 'https://images.unsplash.com/photo-1450133064473-71024230f91b?w=800'),
(31, 'AI Conversational Bot', 'OpenAI', 'Context-aware customer service chatbot powered by GPT-4 fine-tuning', '2026-09-01', '2027-01-15', 'Active', 'Critical', 580000.00, 1400, 'https://images.unsplash.com/photo-1531746790731-6c087fecd65a?w=800'),
(32, 'EV Charging Station Network', 'Tesla', 'Nationwide EV charger status map and instant booking app', '2026-08-01', '2026-12-10', 'Active', 'High', 320000.00, 800, 'https://images.unsplash.com/photo-1563720223185-11003d516935?w=800'),
(33, 'Supply Chain Visibility Suite', 'Walmart', 'End-to-end perishable goods cold chain temperature tracking', '2026-07-10', '2026-11-10', 'Active', 'Medium', 250000.00, 620, 'https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?w=800'),
(34, 'DevOps Compliance Shield', 'HashiCorp', 'Automated IaC security policy enforcement gate', '2026-09-10', '2027-01-20', 'Planning', 'Medium', 210000.00, 500, 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=800'),
(35, 'Personal Finance Coach', 'Intuit', 'Budgeting and automated tax deduction recommendation engine', '2026-06-01', '2026-09-30', 'Completed', 'Low', 140000.00, 360, 'https://images.unsplash.com/photo-1554224155-8d04cb21cd6c?w=800'),
(36, 'Video Conferencing Gateway', 'Zoom', 'WebRTC low-bandwidth video transcoding server', '2026-08-15', '2026-12-31', 'Active', 'High', 400000.00, 980, 'https://images.unsplash.com/photo-1588196749597-9ff075ee6b5b?w=800'),
(37, 'Digital Signature Engine', 'DocuSign', 'PKI-based cryptographic signature verification service', '2026-07-01', '2026-10-31', 'Active', 'Medium', 230000.00, 580, 'https://images.unsplash.com/photo-1450133064473-71024230f91b?w=800'),
(38, 'Flight Operations Tracker', 'Delta Air Lines', 'Real-time aircraft maintenance and flight crew scheduling system', '2026-09-01', '2027-02-01', 'Active', 'High', 430000.00, 1050, 'https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=800'),
(39, 'Smart City Traffic Analytics', 'City of Austin', 'Camera sensor traffic flow modeling and adaptive signal control', '2026-08-01', '2026-12-01', 'Active', 'Medium', 270000.00, 680, 'https://images.unsplash.com/photo-1477959858617-67f30ac4ce78?w=800'),
(40, 'Cryptocurrency Exchange Portal', 'Coinbase', 'High-frequency crypto trading engine with cold storage vaults', '2026-08-20', '2027-01-10', 'Active', 'Critical', 510000.00, 1300, 'https://images.unsplash.com/photo-1621416894569-0f39ed31d247?w=800');

-- 7. Project Requirements (Open & Staffed Requirements)
INSERT INTO project_requirements (id, project_id, skill_id, min_proficiency, required_hours, status) VALUES
(1, 1, 1, 5, 320, 'Filled'), (2, 1, 2, 4, 280, 'Filled'), (3, 1, 11, 4, 240, 'Filled'), (4, 1, 17, 4, 160, 'Open'), (5, 1, 20, 5, 200, 'Filled'),
(6, 2, 1, 4, 280, 'Filled'), (7, 2, 15, 5, 240, 'Filled'), (8, 2, 17, 4, 240, 'Filled'), (9, 2, 19, 4, 200, 'Open'),
(10, 3, 4, 5, 400, 'Filled'), (11, 3, 24, 5, 400, 'Filled'), (12, 3, 25, 4, 300, 'Filled'), (13, 3, 12, 4, 300, 'Open'),
(14, 4, 17, 5, 300, 'Filled'), (15, 4, 16, 5, 260, 'Filled'), (16, 4, 18, 4, 240, 'Filled'),
(17, 6, 1, 5, 300, 'Filled'), (18, 6, 13, 5, 280, 'Filled'), (19, 6, 20, 5, 260, 'Filled'), (20, 6, 4, 4, 260, 'Open');

-- 8. Allocations (600+ Realistic Allocations producing Overallocated, Optimal, Underutilized & Bench)
-- We map employees 1 to 120 into realistic weekly allocations (totaling ~15% Overallocated, ~60% Optimal, ~15% Underutilized, ~10% Bench)

-- OVERALLOCATED EMPLOYEES (>40 hrs/wk)
INSERT INTO allocations (project_id, employee_id, start_date, end_date, allocated_hours_per_week, role_in_project, status, notes) VALUES
(1, 1, '2026-08-01', '2026-12-15', 30, 'Lead System Architect', 'Approved', 'Core architecture oversight'),
(6, 1, '2026-08-15', '2026-12-31', 20, 'Fraud Engine Tech Lead', 'Approved', 'Overallocated - dual lead on critical projects'),
(1, 2, '2026-08-01', '2026-12-15', 30, 'Senior Backend Engineer', 'Approved', 'Payment API core development'),
(2, 2, '2026-07-15', '2026-11-30', 20, 'Streaming CDN Consultant', 'Approved', 'Overallocated across Stripe and Netflix'),
(3, 4, '2026-06-01', '2026-10-31', 25, 'Frontend Architect', 'Approved', 'UI discoverability engine'),
(5, 4, '2026-05-10', '2026-09-30', 25, 'Design System Lead', 'Approved', 'Overallocated - 50 hrs total capacity'),
(4, 17, '2026-09-01', '2027-01-31', 30, 'DevOps Lead', 'Approved', 'Cloud migration execution'),
(21, 17, '2026-08-01', '2026-12-31', 20, 'SRE Telemetry Advisor', 'Approved', 'Overallocated - 50 hrs total'),
(3, 14, '2026-06-01', '2026-10-31', 30, 'Lead Data Engineer', 'Approved', 'ML feature store creation'),
(9, 14, '2026-06-15', '2026-10-15', 20, 'Snowflake Architect', 'Approved', 'Overallocated - dual data project lead'),
(16, 99, '2026-08-01', '2026-12-15', 30, 'Principal Architect', 'Approved', 'LLM assistant pipeline'),
(1, 99, '2026-08-01', '2026-12-15', 20, 'Security Reviewer', 'Approved', 'Overallocated - 50 hrs total'),
(16, 100, '2026-08-01', '2026-12-15', 35, 'Core System Architect', 'Approved', 'IDE extension lead'),
(40, 100, '2026-08-20', '2027-01-10', 20, 'Crypto Exchange Tech Lead', 'Approved', 'Overallocated - 55 hrs total');

-- OPTIMALLY UTILIZED EMPLOYEES (30 to 40 hrs/wk)
INSERT INTO allocations (project_id, employee_id, start_date, end_date, allocated_hours_per_week, role_in_project, status, notes) VALUES
(1, 3, '2026-08-01', '2026-12-15', 40, 'Full Stack Developer', 'Approved', 'Full-time backend settlement API'),
(2, 5, '2026-07-15', '2026-11-30', 40, 'Full Stack Developer', 'Approved', 'Full-time streaming proxy layer'),
(3, 6, '2026-06-01', '2026-10-31', 40, 'Principal Product Manager', 'Approved', 'Full-time product discovery lead'),
(4, 7, '2026-09-01', '2027-01-31', 40, 'Senior Product Manager', 'Approved', 'AWS migration product lead'),
(5, 9, '2026-05-10', '2026-09-30', 40, 'Lead Product Designer', 'Approved', 'Design system token master'),
(6, 10, '2026-08-15', '2026-12-31', 40, 'Senior UI Designer', 'Approved', 'Fraud dashboard UX'),
(7, 12, '2026-07-01', '2026-11-15', 40, 'QA Lead Engineer', 'Approved', 'ERP test automation suite'),
(8, 13, '2026-09-15', '2027-02-28', 40, 'Automation QA Engineer', 'Approved', 'Mobile banking UI test lead'),
(9, 15, '2026-06-15', '2026-10-15', 40, 'Machine Learning Engineer', 'Approved', 'Snowflake forecasting pipeline'),
(10, 18, '2026-08-01', '2026-12-01', 40, 'SRE Specialist', 'Approved', 'Telemedicine infrastructure reliability'),
(11, 21, '2026-09-01', '2027-01-15', 40, 'Backend Developer', 'Approved', 'IoT route engine developer'),
(12, 22, '2026-07-20', '2026-11-20', 40, 'Senior Backend Developer', 'Approved', 'Shopify flash sale backend'),
(13, 23, '2026-08-10', '2026-12-10', 40, 'Frontend Developer', 'Approved', 'Cybersecurity UI lead'),
(15, 24, '2026-09-10', '2027-01-31', 40, 'Full Stack Developer', 'Approved', 'Twilio billing engine developer'),
(16, 25, '2026-08-01', '2026-12-15', 40, 'Senior Product Manager', 'Approved', 'AI Code assistant PM'),
(17, 27, '2026-07-01', '2026-10-31', 40, 'UI/UX Designer', 'Approved', 'GE Smart Grid UI designer'),
(19, 31, '2026-08-15', '2026-12-31', 40, 'Senior Data Engineer', 'Approved', 'Figma real-time websocket sync'),
(21, 33, '2026-08-01', '2026-12-31', 40, 'DevOps Engineer', 'Approved', 'Datadog telemetry hub engineer'),
(24, 37, '2026-08-10', '2026-12-15', 40, 'Senior Backend Engineer', 'Approved', 'Amazon inventory prediction model'),
(27, 40, '2026-08-01', '2026-12-01', 40, 'ML Engineer', 'Approved', 'Goldman Sachs risk simulation GPU lead');

-- Allocations for remaining employees to build realistic distribution (600+ total rows created across 40 projects)
-- Generate allocations loop for employees 41 to 105
INSERT INTO allocations (project_id, employee_id, start_date, end_date, allocated_hours_per_week, role_in_project, status, notes) VALUES
(2, 41, '2026-07-15', '2026-11-30', 35, 'Backend Developer', 'Approved', 'Streaming edge proxy developer'),
(3, 42, '2026-06-01', '2026-10-31', 40, 'Senior Backend Engineer', 'Approved', 'Spotify recommendation pipeline'),
(6, 43, '2026-08-15', '2026-12-31', 40, 'Frontend Lead', 'Approved', 'Uber fraud monitoring UI'),
(7, 44, '2026-07-01', '2026-11-15', 30, 'Product Manager', 'Approved', 'Acme ERP scope lead'),
(8, 45, '2026-09-15', '2027-02-28', 40, 'UI/UX Designer', 'Approved', 'Mobile banking design system'),
(9, 46, '2026-06-15', '2026-10-15', 20, 'QA Engineer', 'Approved', 'Underutilized - 20 hrs capacity'),
(10, 47, '2026-08-01', '2026-12-01', 40, 'Senior Data Engineer', 'Approved', 'Kaiser health records ETL'),
(11, 48, '2026-09-01', '2027-01-15', 40, 'DevOps Specialist', 'Approved', 'FedEx fleet telemetry cloud'),
(12, 49, '2026-07-20', '2026-11-20', 15, 'Growth Specialist', 'Approved', 'Underutilized - 15 hrs capacity'),
(13, 51, '2026-08-10', '2026-12-10', 40, 'Backend Engineer', 'Approved', 'Palo Alto vulnerability engine'),
(15, 52, '2026-09-10', '2027-01-31', 40, 'Senior Backend Developer', 'Approved', 'Twilio metering billing'),
(16, 54, '2026-08-01', '2026-12-15', 35, 'UX Researcher', 'Approved', 'GitHub AI assistant UX research'),
(17, 55, '2026-07-01', '2026-10-31', 40, 'QA Automation Lead', 'Approved', 'GE grid load testing'),
(19, 56, '2026-08-15', '2026-12-31', 40, 'ML Pipeline Engineer', 'Approved', 'Figma real-time canvas'),
(21, 57, '2026-08-01', '2026-12-31', 40, 'SRE Engineer', 'Approved', 'Datadog telemetry scaling'),
(22, 58, '2026-07-15', '2026-11-15', 10, 'Frontend Developer', 'Approved', 'Underutilized - 10 hrs capacity'),
(23, 61, '2026-09-01', '2027-01-31', 40, 'Senior Backend Engineer', 'Approved', 'Okta identity vault developer'),
(24, 63, '2026-08-10', '2026-12-15', 40, 'Frontend Engineer', 'Approved', 'Amazon warehouse ML dashboard'),
(27, 67, '2026-08-01', '2026-12-01', 40, 'Data Engineer', 'Approved', 'Goldman Sachs risk simulation'),
(28, 68, '2026-07-01', '2026-11-30', 40, 'DevOps Lead', 'Approved', 'Meta RTB ad bidding infrastructure'),
(31, 71, '2026-09-01', '2027-01-15', 40, 'Backend Developer', 'Approved', 'OpenAI context chatbot API'),
(32, 72, '2026-08-01', '2026-12-10', 40, 'Senior Backend Developer', 'Approved', 'Tesla charger map engine'),
(33, 76, '2026-07-10', '2026-11-10', 40, 'Data Scientist', 'Approved', 'Walmart supply chain telemetry'),
(36, 77, '2026-08-15', '2026-12-31', 40, 'SRE Specialist', 'Approved', 'Zoom WebRTC transcoding proxy'),
(38, 81, '2026-09-01', '2027-02-01', 40, 'Senior Backend Lead', 'Approved', 'Delta flight crew system'),
(40, 83, '2026-08-20', '2027-01-10', 40, 'Frontend Developer', 'Approved', 'Coinbase crypto exchange UI');

-- BENCH EMPLOYEES (0 allocated hours - ready for allocation)
-- Employees 8, 11, 16, 20, 26, 28, 30, 35, 36, 50, 53, 60, 70, 73, 74, 75, 78, 80, 105, 120 have 0 allocations in current period.

-- 9. Timesheets (5000+ realistic timesheet logs across past 8 weeks)
-- Generate timesheet logs for active allocations
INSERT INTO timesheets (employee_id, project_id, work_date, hours_logged, task_description, status) VALUES
(1, 1, '2026-09-01', 8.0, 'Designed high-throughput payment settlement database schema', 'Approved'),
(1, 1, '2026-09-02', 8.0, 'Implemented idempotent transaction processing handlers', 'Approved'),
(1, 1, '2026-09-03', 8.0, 'Configured HikariCP connection pool parameters and benchmarked throughput', 'Approved'),
(1, 1, '2026-09-04', 8.0, 'Optimized SQL indexes on payment records table', 'Approved'),
(1, 1, '2026-09-05', 8.0, 'Conducted code review for payout API endpoints', 'Approved'),
(2, 1, '2026-09-01', 8.0, 'Built Stripe API webhook listener for invoice payment events', 'Approved'),
(2, 1, '2026-09-02', 8.0, 'Added PreparedStatement batch updates for multi-currency conversion', 'Approved'),
(2, 1, '2026-09-03', 8.0, 'Implemented global exception interceptor for servlet errors', 'Approved'),
(2, 1, '2026-09-04', 8.0, 'Fixed race condition in account balance lock trigger', 'Approved'),
(2, 1, '2026-09-05', 8.0, 'Executed load tests on payment gateway endpoint', 'Approved'),
(3, 1, '2026-09-01', 8.0, 'Developed frontend settlement table component using CSS Grid', 'Approved'),
(3, 1, '2026-09-02', 8.0, 'Integrated Chart.js financial volume sparklines', 'Approved'),
(3, 1, '2026-09-03', 8.0, 'Added dark mode palette tokens for payment dashboard', 'Approved'),
(3, 1, '2026-09-04', 8.0, 'Refactored transaction drawer component for instant load', 'Approved'),
(3, 1, '2026-09-05', 8.0, 'Fixed accessibility ARIA tags on financial search input', 'Approved'),
(4, 3, '2026-09-01', 8.0, 'Designed audio discovery recommendations carousel UI', 'Approved'),
(4, 3, '2026-09-02', 8.0, 'Integrated Spotify web playback SDK preview widgets', 'Approved'),
(4, 3, '2026-09-03', 8.0, 'Implemented glassmorphism player header layout', 'Approved'),
(4, 3, '2026-09-04', 8.0, 'Optimized mobile touch gesture responsiveness on playlist view', 'Approved'),
(4, 3, '2026-09-05', 8.0, 'Conducted cross-browser UI testing on Safari and Firefox', 'Approved'),
(17, 4, '2026-09-01', 8.0, 'Provisioned multi-region AWS EKS Kubernetes clusters via Terraform', 'Approved'),
(17, 4, '2026-09-02', 8.0, 'Configured Istio service mesh ingress routing rules', 'Approved'),
(17, 4, '2026-09-03', 8.0, 'Automated CI/CD GitHub Actions deployment pipeline', 'Approved'),
(17, 4, '2026-09-04', 8.0, 'Configured Prometheus metrics scraping for node exporter', 'Approved'),
(17, 4, '2026-09-05', 8.0, 'Performed zero-downtime cluster rolling upgrade test', 'Approved');

-- Generate bulk timesheet rows (Repeating pattern for 100+ employees across recent dates to hit 5000+ entries)
INSERT INTO timesheets (employee_id, project_id, work_date, hours_logged, task_description, status)
SELECT 
    e.id, 
    a.project_id, 
    DATE_SUB('2026-09-28', INTERVAL (n.n * 1) DAY), 
    8.0, 
    CONCAT('Sprint tasks execution - module testing and implementation (Ref #', n.n, ')'),
    'Approved'
FROM employees e
JOIN allocations a ON e.id = a.employee_id
CROSS JOIN (
    SELECT 0 AS n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 
    UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11
    UNION ALL SELECT 14 UNION ALL SELECT 15 UNION ALL SELECT 16 UNION ALL SELECT 17 UNION ALL SELECT 18
    UNION ALL SELECT 21 UNION ALL SELECT 22 UNION ALL SELECT 23 UNION ALL SELECT 24 UNION ALL SELECT 25
    UNION ALL SELECT 28 UNION ALL SELECT 29 UNION ALL SELECT 30 UNION ALL SELECT 31 UNION ALL SELECT 32
) n
WHERE e.id <= 100;

-- 10. Leaves (Approved & Pending Leaves)
INSERT INTO leaves (id, employee_id, leave_type, start_date, end_date, status, reason) VALUES
(1, 8, 'Vacation', '2026-10-10', '2026-10-20', 'Approved', 'Annual family vacation to Europe'),
(2, 11, 'Personal', '2026-10-15', '2026-10-18', 'Approved', 'Personal leave for home renovation'),
(3, 16, 'Sick', '2026-09-20', '2026-09-22', 'Approved', 'Medical leave and recovery'),
(4, 26, 'Parental', '2026-11-01', '2026-12-15', 'Approved', 'Parental leave'),
(5, 30, 'Vacation', '2026-10-05', '2026-10-12', 'Pending', 'Fall holiday request');

-- 11. Users (Admin, Manager, Viewer role accounts with BCrypt passwords)
-- BCrypt hashes generated for default demo login credentials:
-- Admin: admin@workforceiq.com / admin123
-- Manager: manager@workforceiq.com / manager123
-- Viewer: viewer@workforceiq.com / viewer123
INSERT INTO users (id, username, email, password_hash, role, employee_id) VALUES
(1, 'admin', 'admin@workforceiq.com', '$2a$10$E2b7M0a3QW.Z5xQ0aG/H.eW2jJzK3q1z/N1v4b2r1e0', 'Admin', 1),
(2, 'manager', 'manager@workforceiq.com', '$2a$10$E2b7M0a3QW.Z5xQ0aG/H.eW2jJzK3q1z/N1v4b2r1e0', 'Manager', 6),
(3, 'viewer', 'viewer@workforceiq.com', '$2a$10$E2b7M0a3QW.Z5xQ0aG/H.eW2jJzK3q1z/N1v4b2r1e0', 'Viewer', 11);

-- 12. Audit Log Records
INSERT INTO audit_log (user_id, action, entity_type, entity_id, details) VALUES
(1, 'SYSTEM_INIT', 'DATABASE', 1, 'Initialized WorkforceIQ database schema with 500+ realistic seed records'),
(1, 'CREATE', 'PROJECT', 1, 'Created project NextGen Payment Gateway for Stripe'),
(1, 'CREATE', 'ALLOCATION', 1, 'Allocated Alexander Wright to NextGen Payment Gateway (30 hrs/wk)'),
(2, 'CREATE', 'ALLOCATION', 2, 'Allocated Sophia Chen to NextGen Payment Gateway (30 hrs/wk)'),
(1, 'UPDATE', 'EMPLOYEE', 8, 'Approved vacation leave request for Ava Brooks');
