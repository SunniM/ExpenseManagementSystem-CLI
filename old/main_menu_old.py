import datetime
import os
import sys

import database_modules_old as db

mydb = db.Database()

def login():
    while True:
        username = input("Enter username: ")
        password = input("Enter password: ")
        if mydb.validate_user(username, password):
            print(f"Welcome, {username}!")
            break
        print("Invalid username or password. Try again.")

def add_expense_report():
    while True:
        try:
            date = input("Enter date: ")
            amount = float(input("Enter amount: "))
            category = input("Enter category: ")
            desc = input("Enter description: ")
        except ValueError:
            print("Invalid input. Try again.")
        else:
            mydb.add(date, amount, category, desc)
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
    choice = 0
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
    login()
    while True:
        display_menu()
main()
