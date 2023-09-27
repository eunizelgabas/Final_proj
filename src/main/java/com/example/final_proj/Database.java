package com.example.final_proj;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    public static Connection connectionDB(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/expensetracker" , "root", "");
            return connect;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
