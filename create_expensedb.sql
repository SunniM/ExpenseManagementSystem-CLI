CREATE DATABASE IF NOT EXISTS expensedb;

USE expensedb;

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(25) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role VARCHAR(50) CHECK (
        role in ('MANAGER', 'EMPLOYEE')
    )
);

CREATE TABLE IF NOT EXISTS expenses (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    user_id INTEGER NOT NULL,
    amount REAL NOT NULL,
    description VARCHAR(50) NOT NULL,
    date VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TRIGGER new_expense_approval
AFTER INSERT ON expenses FOR EACH ROW
INSERT INTO approvals
SET
    expense_id = id,
    status = "PENDING";

DROP TABLE approvals;

CREATE TABLE IF NOT EXISTS approvals (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    expense_id INTEGER NOT NULL,
    status VARCHAR(50) CHECK (
        status in (
            'PENDING',
            'APPROVED',
            'DENIED'
        )
    ),
    reviewer INTEGER,
    comment VARCHAR(50),
    review_date VARCHAR(50),
    FOREIGN KEY (expense_id) REFERENCES expenses (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO
    users (username, password, role)
VALUES (
        "admin",
        "admin123",
        "MANAGER"
    ),
    (
        "alice",
        "wonderland",
        "EMPLOYEE"
    );
