package com.revature.expensemanager.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.Properties;

public class ExpenseDB {
    static Connection connection = null;

    public static Connection getConnection() throws SQLException, IOException {
        Properties properties = new Properties();

        InputStream inputStream = ClassLoader.getSystemResourceAsStream("expensedb.properties");
        if (inputStream == null) {
            throw new FileNotFoundException("expensedb.properties not found");
        }

        properties.load(inputStream);

        return DriverManager.getConnection(
                properties.getProperty("url"),
                properties.getProperty("username"),
                properties.getProperty("password"));
    }

}
