package com.ictkerala.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class handles the connection between Java and the MySQL database.
 * It uses JDBC (Java Database Connectivity) to connect.
 */
public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/studentdb"; // studentdb is the DB name
    private static final String USER = "root";  //MySQL username
    private static final String PASSWORD = "12345"; //MySQL password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        }
    }

    /**
     * Establishes and returns a connection to the MySQL database using JDBC.
     * Uses the URL, username, and password defined at the top.
     * This method throws SQLException if the connection fails.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}