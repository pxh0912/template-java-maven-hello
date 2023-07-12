package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class MyAdmin {
    private static final String DB_URL = "jdbc:sqlite:admins.db";

    public boolean login(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Admins WHERE username = ?")) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (password.equals(storedPassword)) {
                    System.out.println("管理员登录成功！");
                    return true;
                } else {
                    System.out.println("密码不正确。");
                }
            } else {
                System.out.println("用户名不存在。");
            }
        } catch (SQLException e) {
            System.out.println("管理员登录失败: " + e.getMessage());
        }

        return false;
    }
}
