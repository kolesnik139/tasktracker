CREATE SCHEMA IF NOT EXISTS tasktracker;
SET SCHEMA tasktracker;

CREATE TABLE IF NOT EXISTS statuses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS tasks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    status_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    active BOOLEAN DEFAULT true,
    FOREIGN KEY (status_id) REFERENCES statuses(id)
);