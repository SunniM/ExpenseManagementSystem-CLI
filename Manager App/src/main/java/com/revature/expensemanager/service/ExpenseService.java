package com.revature.expensemanager.service;

import java.sql.Connection;
import java.util.List;

import com.revature.expensemanager.JDBC.ExpenseJDBC;
import com.revature.expensemanager.exception.ExpenseNotFoundException;
import com.revature.expensemanager.model.Expense;

public class ExpenseService {
    private ExpenseJDBC expenseJDBC;

    public ExpenseService(Connection connection) {
        expenseJDBC = new ExpenseJDBC(connection);
    }

    private static int computeMaxDescLength(List<Expense> expenses) {
        return expenses.stream()
                .map(e -> e.getDescription().length())
                .max(Integer::compareTo)
                .orElse(10);
    }

    public Expense getExpense(int id) throws ExpenseNotFoundException {
        Expense expense = expenseJDBC.get(id);
        if (expense == null)
            throw new ExpenseNotFoundException("Could Not Find Expense");
        return expense;
    }

    public String getExpenseTable() {
        List<Expense> expenses = expenseJDBC.getPendingExpenses();
        int width = computeMaxDescLength(expenses);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%10s "
                + "%10s "
                + "%10s "
                + "%" + width + "s "
                + "%10s\n",
                "ID", "User ID", "Amount", "Description", "Date"));
        for (Expense expense : expenses) {
            sb.append(String.format("%10d "
                    + "%10d "
                    + "%10.2f "
                    + "%" + width + "s "
                    + "%10s\n",
                    expense.getId(),
                    expense.getUserID(),
                    expense.getAmount(),
                    expense.getDescription(),
                    expense.getDate()));
        }
        return sb.toString();
    }

    public String viewByEmployee() {
        return expenseJDBC.getEmployeeReport();
    }

    public String viewByCategory() {
        return expenseJDBC.getCategoryReport();
    }

    public String viewByDate() {
        return expenseJDBC.getDateReport();
    }
}
