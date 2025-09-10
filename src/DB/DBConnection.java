package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/proiect_java";
    private static final String USER = "root";
    private static final String PASS = "root";

    private static DBConnection instance;
    private Connection connection;

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection()
    {
        try {
            if (connection == null || connection.isClosed())
            {
                connection = DriverManager.getConnection(
                        DBConnection.URL,
                        DBConnection.USER,
                        DBConnection.PASS
                );
                System.out.println("Conexiune cu baza de date realizată.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get database connection", e);
        }
        return connection;
    }

    public void closeConnection()
    {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}