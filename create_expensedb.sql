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
    amount DECIMAL(10, 2) NOT NULL,
    description VARCHAR(50) NOT NULL,
    date VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

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

CREATE TABLE IF NOT EXISTS categories (
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS expense_categories (
    expense_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    PRIMARY KEY (expense_id, category_id),
    FOREIGN KEY (expense_id) REFERENCES expenses (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TRIGGER new_expense_approval
AFTER INSERT ON expenses
FOR EACH ROW
BEGIN
    INSERT INTO approvals (expense_id, status)
    VALUES (NEW.id, 'PENDING');
END;

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
    ),
    ("bob", "builder", "EMPLOYEE");

INSERT INTO
    expenses (
        user_id,
        amount,
        description,
        date
    )
VALUES (
        2,
        35,
        "Coffee Date",
        "2025-11-28"
    );

INSERT INTO
    expenses (
        user_id,
        amount,
        description,
        date
    )
VALUES (
        2,
        35,
        "Coffee Date",
        "2025-11-28"
    );

categories (name)
VALUES ("Travel"),
    ("Food"),
    ("Office Supplies"),
    ("Entertainment"),
    ("Miscellaneous");

INSERT INTO
    users (username, password, role)
VALUES ("bob", "builder", "EMPLOYEE");

--- Report By Category
SELECT SUM(e.amount) as Total, c.name as Category
FROM
    expenses e
    JOIN expense_categories ec ON e.id = ec.expense_id
    JOIN categories c ON ec.category_id = c.id
GROUP BY
    c.name;

--- Report By Employee
SELECT u.username as Employee, SUM(e.amount) as Total
FROM expenses e
    JOIN users u on e.user_id = u.id
GROUP BY
    u.id;

--- Report By Date
SELECT MONTH(e.date) as Month, YEAR(e.date) as Year, SUM(e.amount) as Total
FROM expenses e
GROUP BY
    MONTH(e.date),
    YEAR(e.date);
