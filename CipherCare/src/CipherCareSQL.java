import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CipherCareSQL {
    public static Connection getConnection() throws SQLException {
        System.out.println("Attempting to connect to the database...");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/CipherCare";
            String user = "root";
            String password = "root";
            System.out.println("Connection established.");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found.");
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw e;
        }
    }

}
