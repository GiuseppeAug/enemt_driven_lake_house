package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateEdgeTable {

    private static final String URL = "jdbc:postgresql://localhost:5432/dos";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    public static void createEdgeTable() {
        Connection conn = null;
        Statement stmt = null;

        try {
            // Load the PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Establish connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            stmt = conn.createStatement();

            // SQL statement to create the table
            String createTableSQL = "CREATE TABLE IF NOT EXISTS edge_table (" +
                    "machine_id SERIAL PRIMARY KEY, " +
                    "machine_name VARCHAR(100) NOT NULL, " +
                    "timestamp TEXT DEFAULT TO_CHAR(CURRENT_TIMESTAMP, 'YYYY-MM-DD HH24:MI:SS'), " +
                    "oil_pressure TEXT" +
                    ")";
            stmt.executeUpdate(createTableSQL);

            System.out.println("Table 'edge_table' created successfully.");

        } catch (ClassNotFoundException | SQLException e) {
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
