package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class MyAdminManager {
    private Scanner scanner;
    private MyAdminLoginAction adminLoginAction;
    private static final String DB_URL = "jdbc:sqlite:admin_passwords.db";

    public MyAdminManager(Scanner scanner) {
        this.scanner = scanner;
        this.adminLoginAction = new MyAdminLoginAction(scanner, this);
    }

    public boolean login(String username, String password) {
        // Logic to authenticate admin login
        // Implement the actual login process, including validation and database operations
        // Return true if login is successful, otherwise false
        return true; // Always return true for any username and password
    }

    public void managePasswords() {
        while (true) {
            System.out.println("密码管理:");
            System.out.println("1. 修改密码");
            System.out.println("2. 重置用户密码");
            System.out.println("3. 返回上层菜单");
            System.out.print("请输入您的选择: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    changePassword();
                    break;
                case 2:
                    resetUserPassword();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("无效的选择，请重试。");
            }
        }
    }
    public boolean changePassword(String currentPassword, String newPassword) {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement("UPDATE Admins SET password = ? WHERE username = ? AND password = ?")) {
            statement.setString(1, newPassword);
            statement.setString(2, "admin");
            statement.setString(3, currentPassword);
            int rowsAffected = statement.executeUpdate();
    
            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("管理员密码修改失败: " + e.getMessage());
            return false;
        }
    }
    private void changePassword() {
        System.out.print("请输入当前密码: ");
        String currentPassword = scanner.nextLine();

        System.out.print("请输入新密码: ");
        String newPassword = scanner.nextLine();

        // Connect to the database and update the admin's password
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement("UPDATE Admins SET password = ? WHERE username = ? AND password = ?")) {
            statement.setString(1, newPassword);
            statement.setString(2, "admin");
            statement.setString(3, currentPassword);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("管理员密码修改成功！");
            } else {
                System.out.println("当前密码不正确，请重试。");
            }
        } catch (SQLException e) {
            System.out.println("管理员密码修改失败: " + e.getMessage());
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

    public void manageCustomers() {
        // Customer management logic
    }

    public void manageProducts() {
        // Product management logic
    }

    public void adminMenu() {
        while (true) {
            System.out.println("管理员菜单:");
            System.out.println("1. 登录");
            System.out.println("2. 密码管理");
            System.out.println("3. 客户管理");
            System.out.println("4. 商品管理");
            System.out.println("5. 退出登录");
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
                    manageCustomers();
                    break;
                case 4:
                    manageProducts();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("无效的选择，请重试。");
            }
        }
    }

    public boolean registerAdmin(String username, String password) {
        return false; // Registration functionality is not implemented
    }
}
