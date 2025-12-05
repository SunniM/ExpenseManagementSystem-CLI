package com.revature.expensemanager.JDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import com.revature.expensemanager.dao.Dao;
import com.revature.expensemanager.model.User;

public class UserJDBC implements Dao<User> {
    private static final Logger logger = LoggerFactory.getLogger(UserJDBC.class);

    private Connection connection;

    public UserJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addUser'");
    }

    @Override
    public User get(int id) {
        User user = null;
        String query = "SELECT * FROM users WHERE id = ?";
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                user = new User(
                        Integer.parseInt(resultSet.getString("id")),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role"));

        } catch (SQLException e) {
            logger.error("Could not get user: ", e);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users;";
        Statement statement;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("role")));
            }
        } catch (SQLException e) {
            logger.error("Could not get all users: ", e);
        }
        return users;
    }

    @Override
    public void update(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void delete(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeUser'");
    }

}
