package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MyAdminManager {
    private Scanner scanner;
    private MyAdminLoginAction adminLoginAction;
    private static final String DB_URL = "jdbc:sqlite:admin_passwords.db";
    private String currentAdmin; // 当前管理员用户名

    public MyAdminManager(Scanner scanner) {
        this.scanner = scanner;
        this.adminLoginAction = new MyAdminLoginAction(scanner, this);
    }

    public boolean login(String username, String password) {
        // Check if the provided username and password match the admin account
        if (username.equals("admin") && password.equals("12345")) {
            return true;
        } else {
            return false;
        }
    }

    public void setCurrentAdmin(String username) {
        this.currentAdmin = username;
    }

    public String getCurrentAdmin() {
        return currentAdmin;
    }

    public void managePasswords() {
        if (currentAdmin == null) {
            System.out.println("请先登录管理员账号！");
            return;
        }

        while (true) {
            System.out.println("密码管理:");
            System.out.println("1. 重置用户密码");
            System.out.println("2. 修改自身密码");
            System.out.println("3. 返回上层菜单");
            System.out.print("请输入您的选择: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    resetUserPassword();
                    break;
                case 2:
                    changeAdminPassword();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("无效的选择，请重试。");
            }
        }
    }

    private void resetUserPassword() {
        System.out.print("请输入要重置密码的用户名: ");
        String username = scanner.nextLine();

        System.out.print("请输入新密码: ");
        String newPassword = scanner.nextLine();

        // Connect to the database and update the user's password
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement("UPDATE Users SET password = ? WHERE username = ?")) {
            statement.setString(1, newPassword);
            statement.setString(2, username);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("用户密码重置成功！");
            } else {
                System.out.println("找不到该用户，请检查用户名。");
            }
        } catch (SQLException e) {
            System.out.println("用户密码重置失败: " + e.getMessage());
        }
    }

    private void changeAdminPassword() {
        if (currentAdmin == null) {
            System.out.println("请先登录管理员账号！");
            return;
        }

        System.out.print("请输入新密码: ");
        String newPassword = scanner.nextLine();

        // Connect to the database and update the admin's password
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement("UPDATE Admins SET password = ? WHERE username = ?")) {
            statement.setString(1, newPassword);
            statement.setString(2, currentAdmin);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("管理员密码修改成功！");
            } else {
                System.out.println("密码修改失败，请重试。");
            }
        } catch (SQLException e) {
            System.out.println("管理员密码修改失败: " + e.getMessage());
        }
    }

    public void adminMenu() {
        while (true) {
            System.out.println("管理员菜单:");
            System.out.println("1. 登录");
            System.out.println("2. 密码管理");
            System.out.println("3. 退出登录");
            System.out.print("请输入您的选择: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    adminLoginAction.run(null);
                    break;
                case 2:
                    managePasswords();
                    break;
                case 3:
                    setCurrentAdmin(null); // 退出登录，清空当前管理员
                    return;
                default:
                    System.out.println("无效的选择，请重试。");
            }
        }
    }
}
