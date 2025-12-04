package com.revature.expensemanager.JDBC;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.revature.expensemanager.dao.Dao;
import com.revature.expensemanager.model.Expense;

public class ExpenseJDBC implements Dao<Expense> {

    Connection connection;

    public ExpenseJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Expense> get(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public List<Expense> getAll() {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT * FROM expenses;";
        Statement statement;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                expenses.add(new Expense(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("description"),
                        resultSet.getString("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return expenses;
    }

    @Override
    public void save(Expense t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void update(Expense t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Expense t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    public List<Expense> getPendingExpenses() {
        List<Expense> expenses = new ArrayList<>();
        String query = "SELECT * FROM expenses JOIN approvals ON (expenses.id = approvals.expense_id) WHERE approvals.status=\"PENDING\";";
        Statement statement;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                expenses.add(new Expense(
                        resultSet.getInt("id"),
                        resultSet.getInt("user_id"),
                        resultSet.getDouble("amount"),
                        resultSet.getString("description"),
                        resultSet.getString("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return expenses;
    }

    public String getCategoryReport() {
        StringBuilder report = new StringBuilder();
        String query = "SELECT SUM(e.amount) as Total, c.name as Category " +
                "FROM expenses e " +
                "JOIN expense_categories ec ON e.id = ec.expense_id " +
                "JOIN categories c ON ec.category_id = c.id " +
                "GROUP BY c.name;";
        Statement statement;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            report.append(String.format("%-20s %10s\n", "Category", "Total Amount"));
            report.append("----------------------------------------\n");
            while (resultSet.next()) {
                String category = resultSet.getString("Category");
                double total = resultSet.getDouble("Total");
                report.append(String.format("%-20s %10.2f\n", category, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report.toString();
    }

    public String getEmployeeReport() {
        StringBuilder report = new StringBuilder();
        String query = "SELECT u.username as Employee, SUM(e.amount) as Total " +
                "FROM expenses e " +
                "JOIN users u on e.user_id = u.id " +
                "GROUP BY u.id;";
        Statement statement;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            report.append(String.format("%-20s %10s\n", "Employee", "Total Amount"));
            report.append("----------------------------------------\n");
            while (resultSet.next()) {
                String category = resultSet.getString("Employee");
                double total = resultSet.getDouble("Total");
                report.append(String.format("%-20s %10.2f\n", category, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report.toString();
    }

    public String getDateReport() {
        StringBuilder report = new StringBuilder();

        String query = "SELECT MONTH(e.date) as Month, YEAR(e.date) as Year, SUM(e.amount) as Total " +
                "FROM expenses e " +
                "GROUP BY MONTH(e.date), YEAR(e.date);";
        Statement statement;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            report.append(String.format("%-20s %10s\n", "Date", "Total Amount"));
            report.append("----------------------------------------\n");
            while (resultSet.next()) {
                String date = resultSet.getString("Month") + "/" + resultSet.getString("Year");
                double total = resultSet.getDouble("Total");
                report.append(String.format("%-20s %10.2f\n", date, total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report.toString();
    }
}
