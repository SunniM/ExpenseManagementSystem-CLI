package com.revature.expensemanager.service;

import java.sql.Connection;
import java.util.Optional;

import com.revature.expensemanager.JDBC.UserJDBC;
import com.revature.expensemanager.exception.UserNotFoundException;
import com.revature.expensemanager.model.User;

public class UserService {
    private UserJDBC userJDBC;

    public UserService(Connection connection) {
        userJDBC = new UserJDBC(connection);
    }

    public Optional<User> getUser(int id) throws UserNotFoundException {
        Optional<User> optionalUser = userJDBC.get(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User ID could not be verified.");
        }
        return optionalUser;
    }

    public void addUser(User user) {
        userJDBC.save(user);
    }
}
