CREATE SCHEMA tasktracker;
SET SCHEMA tasktracker;

CREATE TABLE statuses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE tasks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    status_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    due_date DATE,
    active BOOLEAN DEFAULT true,
    FOREIGN KEY (status_id) REFERENCES statuses(id)
);