import datetime
import os
import sys

import sql_db as db

mydb = db.Database()

def validate_date(date):
    now = datetime.date.today()
    if date > now:
        raise ValueError("Date cannot be in the future.")

def validate_amount(amount):
    amount = float(amount)
    if amount < 0:
        raise ValueError("Amount cannot be negative.")
    
def validate_description(description):
    if description.empty():
        raise ValueError("Description cannot be empty.")

def login():
    while True:
        username = input("Enter username: ")
        password = input("Enter password: ")
        if mydb.validate_user(username, password):
            print(f"Welcome, {username}!")
            break
        clear_console()
        print("Invalid username or password. Try again.")

def add_expense_report():
    while True:
        try:
            date = input("Enter date (YYYY-MM-DD): ").split('-')
            date = datetime.date(int(date[0]), int(date[1]), int(date[2]))
            validate_date(date)
            amount = input("Enter amount ($25.04): $")
            validate_amount(amount)
            description = input("Enter description: ")
            validate_description(description)
        except ValueError as ve:
            print(ve, " Try again.")
        except IndexError:
            print("Invalid date format. Use YYYY-MM-DD.")
        else:
            mydb.add(date, float(amount), description)
            break

def view_expense_reports():
    mydb.view()

def edit_expense_reports():
    mydb.view()

    while True:
        try:
            index = int(input("Enter index of expense report: "))
            date = input("Enter date: ")
            amount = float(input("Enter amount: "))
            category = input("Enter category: ")
            desc = input("Enter description: ")
        except ValueError:
            print("Invalid input. Try again.")
        else:
            mydb.update(index, date, amount, category, desc)
            # mydb.update(index, "", "", "", "")
            break

def close():
    mydb.close()
    sys.exit()

menu_options = [("Submit New Expense Report" , add_expense_report),
                ("View Pending Expense Reports", view_expense_reports),
                ("Edit Pending Expense Reports", edit_expense_reports),
                ("View All Expense Reports", view_expense_reports),
                ("Exit", close)]

def display_menu():
    choice = -1
    chosen = False
    while not chosen:
        chosen = True
        for i, option in enumerate(menu_options):
            print(f"{i} - {option[0]}")
        try:
            choice = int(input("Select an option: "))
        except ValueError:
            print("Invalid choice. Try again.")
            chosen = False
        if choice < 0 or choice > len(menu_options):
            print("Invalid choice. Try again.")
            chosen = False
        clear_console()
    menu_options[choice][1]()

# Validate date (no future date), amount(no negative amount), and description (not null)
# Add expense limit
def clear_console():
    """Clears the console screen based on the operating system."""
    if os.name == 'nt':  # For Windows
        os.system('cls')
    else:  # For Linux and macOS
        os.system('clear')

def main():
    clear_console()
    user = login()
    clear_console()
    while True:
        display_menu()
main()
