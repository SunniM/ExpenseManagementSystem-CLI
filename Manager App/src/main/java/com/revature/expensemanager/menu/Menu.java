package com.revature.expensemanager.menu;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.expensemanager.service.ExpenseService;
import com.revature.expensemanager.exception.UserNotFoundException;
import com.revature.expensemanager.model.User;
import com.revature.expensemanager.service.ApprovalService;
import com.revature.expensemanager.service.LoginService;

public class Menu {

    private static final Logger logger = LoggerFactory.getLogger(Menu.class);
    private ExpenseService expenseService;
    private ApprovalService approvalService;
    private LoginService loginService;

    private final Scanner scanner = new Scanner(System.in);;
    private User user = new User("", "", "");
    private String[] menuOptions = {
            "1. View Pending Expenses",
            "2. Approve Pending Expense",
            "3. Deny Pending Expense",
            "4. View Reports",
            "0. Exit" };

    public Menu(Connection conn) {
        expenseService = new ExpenseService(conn);
        approvalService = new ApprovalService(conn);
        loginService = new LoginService(conn);
    }

    private final static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else { // Assume Unix-like systems (Linux, macOS)
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (final Exception e) {
            // Handle any exceptions, e.g., print an error message
            System.err.println("Error clearing console: " + e.getMessage());
        }
    }

    private void login() {
        clearConsole();
        while (user.getId() == null) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            try {
                user = loginService.validateLogin(username, password);
            } catch (UserNotFoundException e) {
                System.out.println(e.getMessage() + " Try Again");
            }
        }
        logger.info("User logged in - {}", user);
        clearConsole();
        System.out.println("Welcome " + user.getUsername());
    }

    private void displayOptions() {
        for (String option : menuOptions) {
            System.out.println(option);
        }
    }

    private int getIntegerInput() {
        boolean valid = false;
        int choice = -1;
        while (!valid) {
            System.out.print("> ");
            try {
                choice = scanner.nextInt();
                valid = true;
            } catch (InputMismatchException ime) {
                System.out.println("Please enter an integer value from the options above.");
            } finally {
                scanner.nextLine();
            }
        }
        return choice;
    }

    private String getComment() {
        String description = "";
        boolean valid = false;

        while (!valid) {
            description = getStringInput();
            valid = true;
            if (description.isEmpty()) {
                valid = false;
                System.out.println("Comment Cannot Be Empty");
            }
        }
        return description;
    }

    private String getStringInput() {
        String choice = "";
        System.out.print("> ");
        choice = scanner.nextLine();
        return choice;
    }

    private boolean getConfirmation() {
        System.out.println("Enter (Y) to confirm. Enter (N) to cancel:");

        while (true) {
            String confirmation = scanner.nextLine().trim();
            if (confirmation.equalsIgnoreCase("Y")) {
                return true;
            } else if (confirmation.equalsIgnoreCase("N")) {
                return false;
            } else {
                System.out.println("Invalid scanner. Please enter Y or N:");
            }
        }
    }

    private int chooseOption() {
        int choice = -1;
        boolean valid = false;
        while (!valid) {
            try {
                System.out.println("Select and option: ");
                choice = getIntegerInput();
                if (choice < 0 || choice > menuOptions.length - 1)
                    throw new IllegalArgumentException();
                valid = true;
            } catch (IllegalArgumentException iae) {
                System.out.println("Invalid Selection. Enter a value from 0 to " + (menuOptions.length - 1));
            }
        }
        clearConsole();
        return choice;
    }

    public void run() {
        login();
        boolean done = false;
        int choice;

        while (!done) {
            displayOptions();
            choice = chooseOption();
            switch (choice) {
                case 0:
                    return;
                case 1:
                    System.out.println(expenseService.getExpenseTable());
                    break;
                case 2:
                    approveExpense();
                    break;
                case 3:
                    denyExpense();
                    break;
                case 4:
                    viewReports();
                    break;
                default:
                    break;
            }
        }
    }

    private void viewReports() {
        System.out.println("View Reports");
        System.out.println("1. View by Employee");
        System.out.println("2. View by Category");
        System.out.println("3. View by Date");
        System.out.println("Select Report Type:");
        int reportType = -1;
        while (reportType < 1 || reportType > 3) {
            reportType = getIntegerInput();
            if (reportType < 1 || reportType > 3)
                System.out.println("Please enter integer value from options above.");
        }
        String report;
        switch (reportType) {
            case 1:
                report = expenseService.viewByEmployee();
                break;
            case 2:
                report = expenseService.viewByCategory();
                break;
            case 3:
                report = expenseService.viewByDate();
                break;
            default:
                report = "Invalid Report Type.";
                break;
        }
        clearConsole();
        System.out.println(report);
    }

    private void denyExpense() {
        System.out.println(expenseService.getExpenseTable());
        System.out.println("Deny Expense Report");
        System.out.println("Enter Expense ID:");
        int expenseID = getIntegerInput();
        System.out.println("Enter Comment");
        String comment = getComment();
        boolean confirmed = getConfirmation();
        clearConsole();

        if (confirmed) {
            approvalService.denyExpense(expenseID, user.getId(), comment);
            return;
        }
        System.out.println("Canceled.");
    }

    private void approveExpense() {
        System.out.println(expenseService.getExpenseTable());
        System.out.println("Approve Expense Report");
        System.out.println("Enter Expense ID");
        int expenseID = getIntegerInput();
        System.out.println("Enter Comment");
        String comment = getComment();
        boolean confirmed = getConfirmation();
        clearConsole();

        if (confirmed) {
            approvalService.approveExpense(expenseID, user.getId(), comment);
            return;
        }
        System.out.println("Canceled.");
    }
}
