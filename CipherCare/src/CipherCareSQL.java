import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CipherCareSQL {
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/CipherCare"; // Update with your database URL
            String user = "root";
            String password = "root";
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found.");
        }
    }
}
