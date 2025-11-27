import datetime
import pandas as pd
import os
import sys

import sql_db as db

def clear_console():
    if os.name == 'nt':  # For Windows
        os.system('cls')
    else:  # For Linux and macOS
        os.system('clear')

def validate_date(date):
    message = "Invalid Date Format"
    if len(date) < 3:
        raise ValueError(message)
    try:
        date = [int(d) for d in date]
        date = datetime.date(date[0], date[1], date[2])
    except ValueError:
        raise ValueError("Invalid Date Format")
    except IndexError:
        raise ValueError("Invalid Date Format")
    
    now = datetime.date.today()
    if date > now:
        raise ValueError("Date cannot be in the future.")
    return date

def validate_amount(amount, limit):
    amount = float(amount)
    if amount < 0:
        raise ValueError("Amount cannot be negative.")
    if amount > limit:
        raise ValueError(f"Amount cannot exceed limit: ${limit}")
    
def validate_description(description):
    if not description:
        raise ValueError("Description cannot be empty.")

class EmployeeMenu:

    def __init__(self):
        self.db = db.Database()
        self.menu_options = [
                ("Submit New Expense Report" , self.add_expense),
                ("View Pending Expense Reports", self.view_pending),
                ("Edit Pending Expense Reports", self.edit_expense),
                ("View All Expense Reports", self.view_all),
                ("Exit", self.close)]

    def add_expense(self):
        print("Submitting New Expense Report: ")
        while True:
            try:
                date_input = input("Enter date (YYYY-MM-DD): ").split('-')
                date = validate_date(date_input)
                amount = input("Enter amount ($25.04): $")
                validate_amount(amount, 500)
                description = input("Enter description: ")
                validate_description(description)
            except ValueError as ve:
                clear_console()
                print("Submitting New Expense Report: ")
                print(ve, " Try again.")
            except IndexError:
                clear_console()
                print("Submitting New Expense Report: ")
                print("Invalid date format. Use YYYY-MM-DD.")
            else:
                result = self.db.add_expense(self.user[0], float(amount), description, date)
                break
        if result:
            return "Expense Successfully Added"
        return "Could Not Add Expense Try Again"

    def edit_expense(self):
        print(self.view_pending())
        print()
        while True:
            try:
                expense_id = int(input("Enter ID of expense report: "))
                expense = self.db.get_pending_expense_by_id(self.user[0], expense_id)
                date_input = input("Enter date. (YYYY-MM-DD): ").split('-')
                date = validate_date(date_input)
                amount = input("Enter amount ($25.04): $")
                validate_amount(amount, 500)
                description = input("Enter description: ")
                validate_description(description)
            except ValueError:
                print("Invalid input. Try again.")
            else:
                result = self.db.edit_expense(expense_id, self.user[0], amount, description, date)
                break
        if result:
            return "Expense Sucessfully Updated"
        return "Could Not Update Expense. Please Try Again"

            
    def view_pending(self):
        expenses = self.db.get_pending_expenses(self.user[0])
        expenses = pd.DataFrame.from_records(expenses, columns=['ID', 'Amount', 'Description', 'Date', 'Status'])
        expenses.set_index('ID', inplace=True)
        return expenses

    def view_all(self):
        expenses = self.db.get_all_expenses(self.user[0])
        expenses = pd.DataFrame.from_records(expenses, columns=['ID', 'Amount', 'Description', 'Date', 'Status'])
        expenses.set_index('ID', inplace=True)
        return expenses
            
    def login(self):
        while True:
            username = input("Enter username: ")
            password = input("Enter password: ")
            self.user = self.db.validate_user(username, password)
            if self.user:
                clear_console()
                print(f"Welcome, {username}!")
                return
            print("Invalid username or password. Try again.")

    def run(self):
        clear_console()
        self.login()
        clear_console()
        while True:
            self.display_menu()
    
    def display_menu(self):
        choice = -1
        chosen = False
        print()
        while not chosen:
            chosen = True
            for i, option in enumerate(self.menu_options):
                print(f"{i} - {option[0]}")
            try:
                choice = int(input("Select an option: "))
            except ValueError:
                clear_console()
                print("Invalid choice. Try again.")
                chosen = False
            except IndexError:
                clear_console()
                print("Invalid choice. Try again.")
                chosen = False
            if choice < 0 or choice > len(self.menu_options):
                clear_console()
                print("Invalid choice. Try again.")
                chosen = False
        clear_console()
        result = self.menu_options[choice][1]()
        clear_console()

        print(result)
        
        
    def close(self):
        self.db.close()
        sys.exit()



if __name__ == "__main__":
    menu = EmployeeMenu()
    menu.run()
