import sqlite3
def initialize():
    connection = sqlite3.connect('../expenses.db')
    cursor = connection.cursor()
    cursor.execute('''CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    role TEXT CHECK(role in ('MANAGER', 'EMPLOYEE')));
                ''')
    cursor.execute('''CREATE TABLE IF NOT EXISTS expenses (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id INTEGER NOT NULL,
                    amount REAL NOT NULL,
                    description TEXT NOT NULL,
                    date TEXT NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id));
                ''')
    cursor.execute('''CREATE TABLE IF NOT EXISTS approvals (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    expense_id INTEGER NOT NULL,
                    status TEXT CHECK(status in ('PENDING', 'APPROVED', 'DENIED')),
                    reviewer INTEGER,
                    comment TEXT,
                    review_date TEXT);
                ''')

    cursor.execute('''INSERT INTO users (username, password, role) VALUES (?, ?, ?);''',
               ("admin", "admin123", "MANAGER"))
    cursor.execute('''INSERT INTO users (username, password, role) VALUES (?, ?, ?);''',
               ("alice", "wonderland", "EMPLOYEE"))

    connection.commit()
    connection.close()
