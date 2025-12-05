package com.revature.expensemanager.JDBC;

import com.revature.expensemanager.dao.Dao;
import com.revature.expensemanager.model.Approval;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApprovalJDBC implements Dao<Approval> {
    private static final Logger logger = LoggerFactory.getLogger(ApprovalJDBC.class);
    private Connection connection;

    public ApprovalJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Approval get(int id) {
        Approval approval = null;
        String query = "SELECT * FROM approvals WHERE id = ?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                approval = new Approval(
                        Integer.parseInt(resultSet.getString("id")),
                        Integer.parseInt(resultSet.getString("expense_id")),
                        resultSet.getString("status"),
                        Integer.parseInt(resultSet.getString("reviewer")),
                        resultSet.getString("comment"),
                        resultSet.getString("review_date"));

        } catch (SQLException e) {
            logger.error("Could not get approval: ", e);
        }
        return approval;
    }

    public Approval getByExpenseID(int expenseID) {
        Approval approval = null;
        String query = "SELECT * FROM approvals WHERE expense_id = ?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, expenseID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                approval = new Approval(
                        resultSet.getInt("id"),
                        resultSet.getInt("expense_id"),
                        resultSet.getString("status"),
                        resultSet.getInt("reviewer"),
                        resultSet.getString("comment"),
                        resultSet.getString("review_date"));

        } catch (SQLException e) {
            logger.error("Could not get approval: ", e);
        }
        return approval;
    }

    @Override
    public List<Approval> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public void save(Approval t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void update(Approval t) {
        // TODO Auto-generated method stub
        String query = "UPDATE approvals SET status = ?, reviewer = ?, comment = ?, review_date = ? WHERE id = ?;";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, t.getStatus());
            preparedStatement.setInt(2, t.getReviewer());
            preparedStatement.setString(3, t.getComment());
            preparedStatement.setString(4, t.getReviewDate());
            preparedStatement.setInt(5, t.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Approval Updated");
    }

    @Override
    public void delete(Approval t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
