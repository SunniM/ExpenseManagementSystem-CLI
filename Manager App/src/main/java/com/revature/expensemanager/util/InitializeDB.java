package com.revature.expensemanager.util;

import java.io.IOException;
import java.sql.SQLException;

import com.revature.expensemanager.model.User;
import com.revature.expensemanager.service.UserService;

public class InitializeDB {
    public static void main(String[] args) throws SQLException, IOException {
        UserService userService = new UserService(ExpenseDB.getConnection());
        userService.addUser(new User("admin", "password", "MANAGER"));
        userService.addUser(new User("alice", "wonderland", "EMPLOYEE"));
        userService.addUser(new User("bob", "builder", "EMPLOYEE"));
    }

}
