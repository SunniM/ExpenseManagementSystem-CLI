package com.revature.expensemanager.service;

import java.sql.Connection;
import java.util.List;

import com.revature.expensemanager.JDBC.UserJDBC;
import com.revature.expensemanager.exception.UserNotFoundException;
import com.revature.expensemanager.model.User;

public class LoginService {
    // private static final Connection connection = DbConnection.dbConnection();
    private UserJDBC userJDBC;

    public LoginService(Connection conn) {
        userJDBC = new UserJDBC(conn);
    }

    public User validateLogin(String username, String password) throws UserNotFoundException {
        List<User> users = userJDBC.getAll();
        for (User user : users) {
            if (user.getRole().equals("MANAGER")
                    && user.getUsername().equals(username)
                    && user.getPassword().equals(password))
                return user;
        }
        throw new UserNotFoundException("Incorrect username or password");
    }
}
