package org.example;
import java.sql.*;

public class DatabaseQueryExample {
    private static final String DB_URL = "jdbc:sqlite:admin_passwords.db";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Admins")) {

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                System.out.println("Username: " + username + ", Password: " + password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
