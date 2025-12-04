package com.revature.expensemanager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revature.expensemanager.menu.Menu;
import com.revature.expensemanager.util.ExpenseDB;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Connection conn;
        try {
            conn = ExpenseDB.getConnection();
            logger.info("Connection Established");
            Menu menu = new Menu(conn);
            menu.run();
        } catch (SQLException | IOException e) {
            logger.error("Connection Failed");
        }

    }

}
