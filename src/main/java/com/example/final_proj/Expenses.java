package com.example.final_proj;

import javafx.beans.property.IntegerProperty;

import java.sql.Date;

public class Expenses {
    public String category;
    public Date date;
    public String description;

    public double amount;

    public int id;

    public Expenses(int id,String category, Date date, String description, double amount) {
        this.id = id;
        this.category = category;
        this.date = date;
        this.description = description;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
