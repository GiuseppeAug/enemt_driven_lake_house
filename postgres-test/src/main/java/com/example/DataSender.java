// package com.example;

// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.SQLException;
// import java.util.Random;

// public class DataSender {

//     private static final String URL = "jdbc:postgresql://localhost:5432/dos";
//     private static final String USER = "admin";
//     private static final String PASSWORD = "admin";

//     public static void sendData() {
//         Connection conn = null;
//         PreparedStatement pstmt = null;

//         try {
//             // Load the PostgreSQL driver
//             Class.forName("org.postgresql.Driver");

//             // Establish connection
//             conn = DriverManager.getConnection(URL, USER, PASSWORD);

//             // SQL statement to insert data
//             String insertSQL = "INSERT INTO edge_table (machine_name, oil_pressure) VALUES (?, ?)";
//             pstmt = conn.prepareStatement(insertSQL);

//             // Random generator for simulating sensor data
//             Random random = new Random();

//             while (true) { // Infinite loop to send data every 5 seconds
//                 // Simulate machine data
//                 String machineName = "Machine-" + (random.nextInt(100) + 1);
//                 double oilPressure = 50 + (100 - 50) * random.nextDouble();

//                 // Set data in the prepared statement
//                 pstmt.setString(1, machineName);
//                 pstmt.setDouble(2, oilPressure);

//                 // Execute the insertion
//                 int rowsAffected = pstmt.executeUpdate();
//                 System.out.println("Data inserted: " + rowsAffected + " | Machine: " + machineName + " | Oil Pressure: "
//                         + oilPressure);

//                 // Wait for 5 seconds before sending the next data
//                 Thread.sleep(5000);
//             }

//         } catch (ClassNotFoundException | SQLException | InterruptedException e) {
//             e.printStackTrace();
//         } finally {
//             // Clean up
//             try {
//                 if (pstmt != null)
//                     pstmt.close();
//                 if (conn != null)
//                     conn.close();
//             } catch (SQLException e) {
//                 e.printStackTrace();
//             }
//         }
//     }
// }
package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class DataSender {

    private static final String URL = "jdbc:postgresql://localhost:5432/dos";
    private static final String USER = "admin";
    private static final String PASSWORD = "admin";

    public static void sendData() {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // Load the PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Establish connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // SQL statement to insert data
            String insertSQL = "INSERT INTO edge_table (machine_name, oil_pressure) VALUES (?, ?)";
            pstmt = conn.prepareStatement(insertSQL);

            // Random generator for simulating sensor data
            Random random = new Random();

            while (true) { // Infinite loop to send data every 5 seconds
                // Simulate machine data
                String machineName = "Machine-" + (random.nextInt(100) + 1);
                double oilPressure = 50 + (100 - 50) * random.nextDouble();

                // Set data in the prepared statement
                pstmt.setString(1, machineName);
                pstmt.setDouble(2, oilPressure);

                // Execute the insertion
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Data inserted: " + rowsAffected + " | Machine: " + machineName + " | Oil Pressure: "
                        + oilPressure);

                // Wait for 5 seconds before sending the next data
                Thread.sleep(5000);
            }

        } catch (ClassNotFoundException | SQLException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Clean up
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
