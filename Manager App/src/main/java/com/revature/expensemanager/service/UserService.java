package com.revature.expensemanager.service;

import java.sql.Connection;

import com.revature.expensemanager.JDBC.UserJDBC;
import com.revature.expensemanager.exception.UserNotFoundException;
import com.revature.expensemanager.model.User;

public class UserService {
    private UserJDBC userJDBC;

    public UserService(Connection connection) {
        userJDBC = new UserJDBC(connection);
    }

    public User getUser(int id) throws UserNotFoundException {
        User user = userJDBC.get(id);
        if (user == null) {
            throw new UserNotFoundException("User ID could not be verified.");
        }
        return user;
    }

    public void addUser(User user) {
        userJDBC.save(user);
    }
}
