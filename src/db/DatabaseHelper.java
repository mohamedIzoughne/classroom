package db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/est";  // Your DB name
    private static final String USER = "root";  // Default MySQL username
    private static final String PASSWORD = "";  // Default MySQL password (usually empty)

    // Method to establish connection
    public static Connection connect() {
        try {
            // Register the JDBC driver and open a connection
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            return connection;
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}
