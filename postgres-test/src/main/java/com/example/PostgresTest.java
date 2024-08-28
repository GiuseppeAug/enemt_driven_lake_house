package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PostgresTest {

    private static final String URL = "jdbc:postgresql://localhost:5432/dos";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    public static void createTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            // Establish connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.createStatement();

            // Create a table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS test_table (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL" +
                    ")";
            stmt.executeUpdate(createTableSQL);

            System.out.println("Table 'test_table' created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Clean up
            try {
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
