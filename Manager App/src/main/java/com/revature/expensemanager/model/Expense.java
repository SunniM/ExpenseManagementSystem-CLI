package com.revature.expensemanager.model;

import java.util.List;

public class Expense {
    private int id;
    private int userID;
    private double amount;
    private String description;
    private String date;

    public Expense(int id, int userID, double amount, String description, String date) {
        this.id = id;
        this.userID = userID;
        this.amount = amount;
        this.description = description;
        this.date = date;

    }

    public int getId() {
        return id;
    }

    public int getUserID() {
        return userID;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + String.format("{id=%d, userID=%d, amount=%f, description=%s, date=%s}",
                        id, userID, amount, description, date);
    }
}
