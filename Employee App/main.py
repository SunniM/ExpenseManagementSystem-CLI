import datetime
import mysql
import pandas as pd
import os
import sys

# import sql_db as db
import mysql_db as mdb

def clear_console(subtitle=''):
    if os.name == 'nt':  # For Windows
        os.system('cls')
    else:  # For Linux and macOS
        os.system('clear')
    print('================================')
    print(f'Employee Expense Portal{subtitle}')
    print('===============================')

def wait_for_user():
    input('\nPress Enter To Continue')

def validate_id(id):
    try:
        id = int(id)
    except ValueError:
        raise ValueError('Invalid ID')
    if id < 0:
        raise ValueError('Invalid ID')
    return id

def get_valid_id(prompt='Enter an ID Value: '):
    while True:
        try:
            id = input(prompt)
            id = validate_id(id)
        except ValueError as ve:
            print(ve, ' Try Again')
        else: return id
        

def validate_date(date):
    date = date.split('-')
    try:
        date = [int(d) for d in date]
        date = datetime.date(date[0], date[1], date[2])
    except ValueError:
        raise ValueError('Invalid Date Format')
    except IndexError:
        raise ValueError('Invalid Date Format')
    
    now = datetime.date.today()
    if date > now:
        raise ValueError('Date cannot be in the future.')
    return date

def get_valid_date(prompt='Enter date (YYYY-MM-DD): '):
     while True:
        try:
            date = input(prompt)
            date = validate_date(date)
        except ValueError as ve:
            print(ve, ' Try Again')
        else: return date


def validate_amount(amount, limit):
    try: amount = float(amount)
    except ValueError: raise ValueError('Please enter a enter a monetary value as a number.')
    if amount <= 0:
        raise ValueError('Amount must be greater than $0.')
    if amount > limit:
        raise ValueError(f'Amount cannot exceed limit: ${limit}')
    return amount

def get_valid_amount(prompt='Enter amount ($49.97): $'):
    while True:
        try:
            amount = input(prompt)
            amount = validate_amount(amount, 5000)
        except ValueError as ve:
            print(ve, ' Try Again.')
        else: return amount
    
def validate_description(description):
    if not description:
        raise ValueError('Description cannot be empty.')
    
def get_valid_description(prompt='Enter description: '):
    while True:
        try:
            description = input(prompt)
            validate_description(description)
        except ValueError as ve:
            print(ve, ' Try Again.')
        else: return description

class EmployeeMenu:

    def __init__(self):
        # self.db = db.Database()
        try:
            self.db = mdb.Database()
        except mysql.connector.errors.InterfaceError:
            print('Could not connect to database.')
            sys.exit()
        self.menu_options = [
                ('Exit', self.close),
                ('View Pending Expense Reports', self.view_pending),
                ('View All Expense Reports', self.view_all),
                ('Submit New Expense Report' , self.add_expense),
                ('Edit Pending Expense Reports', self.edit_expense),
                ('Delete Pending Expense Reports', self.delete_expense),
        ]
        
    def get_valid_category(self):
        categories = self.db.get_expense_categories()

        def validate_choice(choice):
            try:
                choice = int(choice)
            except ValueError:
                raise ValueError(f"Enter a number from 1 to {len(categories)}")
            if choice < 1 or choice > len(categories):
                raise ValueError(f"Enter a number from 1 to {len(categories)}")
            else: return choice - 1

        print('Select Expense Category:')
        for i, category in enumerate(categories, start=1):
            print(f'{i} - {category[1]}')
    
        while True:
            try:
                choice = (input('Enter choice: '))
                id = validate_choice(choice)
            except ValueError as ve:
                print(ve, ' Try again.')
            else:
                return categories[id][0]
                

    def add_expense(self):
        print()

        date = get_valid_date()
        amount = get_valid_amount()
        description = get_valid_description()
        category_id = self.get_valid_category()
        result = self.db.add_expense(self.user[0], amount, description, date, category_id)
        if result:
            return 'Expense Successfully Added'
        return 'Could Not Add Expense Try Again'

    def edit_expense(self):
        print(self.view_pending())
        print()
        expense = None
        while expense is None:
            try:
                expense_id = get_valid_id('Enter ID of expense report: ')
                expense = self.db.get_pending_expense_by_id(self.user[0], expense_id)
                if(expense is None):
                    raise ValueError('Invalid ID')
            except ValueError as ve:
                print(ve, ' Try Again')

        def get_field_update(current_val, prompt, validator):
            while True:
                try:
                    user_input = input(f'{prompt} or press <Enter> to skip.').strip()
                    if user_input == '':
                        return current_val
                    result = validator(user_input)
                    return result if not None else user_input
                except ValueError as ve:
                    print(ve, ' Try Again.')
                

        amount = get_field_update(expense[1], 'Enter amount ($25.04)', lambda x : validate_amount(x, 5000))
        description = get_field_update(expense[2], 'Enter description', validate_description)
        date = get_field_update(expense[3], 'Enter date (YYYY-MM-DD)', validate_date)

        result = self.db.edit_expense(expense_id, self.user[0], amount, description, date)
        return 'Expense Sucessfully Updated' if result else 'Expense NOT Updated'

    def delete_expense(self):
        print(self.view_pending())
        print()
        expense_id = get_valid_id('Enter ID of expense report to delete: ')
        result = self.db.delete_expense(self.user[0], expense_id)
        return 'Expense Successfully Deleted' if result else 'Could Not Delete Expense. Please Try Again'
            
    def view_pending(self):
        expenses = self.db.get_pending_expenses(self.user[0])
        expenses = pd.DataFrame.from_records(expenses, columns=['ID', 'Amount', 'Description', 'Date', 'Status'])
        expenses.set_index('ID', inplace=True)
        return expenses

    def view_all(self):
        expenses = self.db.get_all_expenses(self.user[0])
        expenses = pd.DataFrame.from_records(expenses, columns=['ID', 'Amount', 'Description', 'Date', 'Status', 'Comment'])
        expenses.set_index('ID', inplace=True)
        return expenses
            
    def login(self):
        sub = ': Login'
        clear_console(sub)
        print()
        while True:
            username = input('Enter username: ')
            password = input('Enter password: ')
            self.user = self.db.validate_user(username, password)
            if self.user:
                return
            clear_console(sub)
            print('Invalid username or password. Try again.')
    
    def display_menu(self, prev_result=''):
        sub = ': Main Menu'
        clear_console(sub)
        print(prev_result)
        print()

        for i, option in enumerate(self.menu_options[1:], start=1):
            print(f'{i} - {option[0]}')
        print(f'0 - {self.menu_options[0][0]}')

        try:
            choice = int(input('Select an option: '))
        except ValueError:
            print('Invalid choice. Please Enter A Number.')
            wait_for_user()
            return prev_result

        if choice < 0 or choice >= len(self.menu_options):
            print(f'Invalid choice. Please enter a number between 0 and {len(self.menu_options)-1}')
            wait_for_user()
            return prev_result
        
        clear_console(f': {self.menu_options[choice][0]}')
        return self.menu_options[choice][1]()

    def run(self):
        self.login()
        result = self.display_menu(f'Welcome, {self.user[1].upper()}!')
        while True:
            result = self.display_menu(result) 
        

    def close(self):
        self.db.close()
        sys.exit()

if __name__ == '__main__':
    menu = EmployeeMenu()
    menu.run()
