import mysql.connector
import pandas as pd
import sqlite3


# import init_db
class Database:
    def __init__(self):
        self.connection = mysql.connector.connect(
        host="localhost",
        user="root",
        password="password",
        database="expensedb",
        autocommit=True
    )
        print("Connection successful!")
        self.cursor = self.connection.cursor()

    def validate_user(self, username, password):
        self.cursor.execute("SELECT * FROM users WHERE username = %s AND password = %s", (username, password))
        return self.cursor.fetchone()
    
    def get_expense_categories(self):
        self.cursor.execute("SELECT * FROM categories")
        return self.cursor.fetchall()
    
    def add_expense(self, user_id, amount, description, date, category_id):
        self.cursor.execute("INSERT INTO expenses (user_id, amount, description, date) VALUES(%s, %s, %s, %s)", (user_id, amount, description, date))
        expense_id = self.cursor.lastrowid
        self.cursor.execute("INSERT INTO expense_categories (expense_id, category_id) VALUES(%s, %s)", (expense_id, category_id))     
        self.connection.commit()
        return self.cursor.rowcount

    def edit_expense(self, expense_id, user_id, amount, description, date):
        self.cursor.execute('''UPDATE expenses
                            SET amount=%s, description=%s, date=%s
                            WHERE id = %s AND
                            user_id = %s
                            ''', (amount, description, date, expense_id, user_id))
        self.connection.commit()
        return self.cursor.rowcount

    def delete_expense(self, user_id, expense_id):
        self.cursor.execute('DELETE FROM expenses WHERE id=%s AND user_id=%s', (expense_id, user_id))
        return self.cursor.rowcount
    
    def get_pending_expenses(self, user_id):
        self.cursor.execute('''SELECT e.id, e.amount, e.description, e.date, a.status 
                               FROM expenses AS e INNER JOIN approvals AS a on a.expense_id = e.id 
                               WHERE e.user_id = %s AND a.status = "PENDING"''', (user_id,))
        return self.cursor.fetchall()
    
    def get_pending_expense_by_id(self, user_id, expense_id):
        self.cursor.execute('''SELECT e.id, e.amount, e.description, e.date
                                FROM expenses AS e
                                WHERE 
                                    e.user_id = %s AND
                                    e.id = %s;
                            ''', (user_id, expense_id))
        return self.cursor.fetchone()
    
    def get_all_expenses(self, user_id):
        self.cursor.execute('''SELECT e.id, e.amount, e.description, e.date, a.status, a.comment
                               FROM expenses AS e
                               JOIN approvals AS a on a.expense_id = e.id 
                               WHERE e.user_id = %s''', (user_id,))
        return self.cursor.fetchall()
    
    def close(self):
        self.connection.commit()
        self.connection.close()


if __name__ == "__main__":
    database = Database()
    user = database.validate_user('admin', 'admin123')
    print(user)
    print(type(user[0]))
    database.add_expense(user[0], 500.00, "Client Dinner", "2025-11-20")

    expenses = database.get_pending_expenses(user[0])
    expenses = pd.DataFrame.from_records(expenses, columns=['ID', 'Amount', 'Description', 'Date', 'Status'])
    expenses.set_index('ID', inplace=True)
    print(expenses)

    database.cursor.execute("SELECT * from approvals")
    approvals = database.cursor.fetchall()
    for a in approvals:
        print(a)






