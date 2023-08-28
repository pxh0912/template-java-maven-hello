package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        if (username.equals("admin") && password.equals("777")) {
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
                System.out.print("请输入新密码: ");
                String newPassword = scanner.nextLine();
                changeAdminPassword(newPassword);
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

   
    public void adminMenu() {
        while (true) {
            System.out.println("管理员菜单:");
            System.out.println("0. 登录");
            System.out.println("1. 密码管理");
            System.out.println("2. 客户管理");
            System.out.println("3. 商品管理");
            System.out.println("4. 退出登录");
            System.out.print("请输入您的选择: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 0:
                    adminLoginAction.run(null);
                break;
                case 1:
                    managePasswords();
                    break;
                case 2:
                    manageCustomers();
                    break;
                case 3:
                    manageProducts();
                    break;
                case 4:
                    setCurrentAdmin(null); // 退出登录，清空当前管理员
                    return;
                default:
                    System.out.println("无效的选择，请重试。");
            }
        }
    }
    

    public void changeAdminPassword(String newPassword) {
        if (currentAdmin == null) {
            System.out.println("请先登录管理员账号！");
            return;
        }
    
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement("UPDATE Admins SET password = ? WHERE username = ?")) {
            statement.setString(1, newPassword);
            statement.setString(2, currentAdmin);
            int rowsAffected = statement.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("管理员密码修改成功！");
            } else {
                System.out.println("管理员密码修改成功！");
            }
        } catch (SQLException e) {
            System.out.println("管理员密码修改失败: " + e.getMessage());
        }
    }
    public void manageCustomers() {
        if (currentAdmin == null) {
            System.out.println("请先登录管理员账号！");
            return;
        }
    
        while (true) {
            System.out.println("客户管理:");
            System.out.println("1. 列出所有客户信息");
            System.out.println("2. 删除客户信息");
            System.out.println("3. 查询客户信息");
            System.out.println("4. 返回上层菜单");
            System.out.print("请输入您的选择: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 1:
                    listAllCustomers();
                    break;
                case 2:
                    deleteCustomer();
                    break;
                case 3:
                    searchCustomer();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("无效的选择，请重试。");
            }
        }
    }
    
    private void listAllCustomers() {
        // 连接数据库并从 Customers 表中获取所有客户信息
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Customers")) {
    
            System.out.println("所有客户信息:");
            while (resultSet.next()) {
                int customerId = resultSet.getInt("id");
                String customerName = resultSet.getString("name");
                String customerEmail = resultSet.getString("email");
                // 输出客户信息
                System.out.println("ID: " + customerId + ", 姓名: " + customerName + ", 邮箱: " + customerEmail);
            }
        } catch (SQLException e) {
            System.out.println("获取客户信息失败: " + e.getMessage());
        }
    }
    
    private void deleteCustomer() {
        System.out.print("请输入要删除的客户ID: ");
        int customerIdToDelete = scanner.nextInt();
        scanner.nextLine();
    
        // 连接数据库并删除指定客户
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Customers WHERE id = ?")) {
            statement.setInt(1, customerIdToDelete);
            int rowsAffected = statement.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("客户删除成功！");
            } else {
                System.out.println("找不到指定客户，请检查ID。");
            }
        } catch (SQLException e) {
            System.out.println("删除客户失败: " + e.getMessage());
        }
    }
    
    private void searchCustomer() {
        System.out.print("请输入要查询的客户姓名: ");
        String customerNameToSearch = scanner.nextLine();
    
        // 连接数据库并查询指定客户
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Customers WHERE name = ?")) {
            statement.setString(1, customerNameToSearch);
            ResultSet resultSet = statement.executeQuery();
    
            while (resultSet.next()) {
                int customerId = resultSet.getInt("id");
                String customerName = resultSet.getString("name");
                String customerEmail = resultSet.getString("email");
                // 输出客户信息
                System.out.println("ID: " + customerId + ", 姓名: " + customerName + ", 邮箱: " + customerEmail);
            }
        } catch (SQLException e) {
            System.out.println("查询客户失败: " + e.getMessage());
        }
    }
    public void manageProducts() {
        if (currentAdmin == null) {
            System.out.println("请先登录管理员账号！");
            return;
        }
    
        while (true) {
            System.out.println("商品管理:");
            System.out.println("1. 列出所有商品信息");
            System.out.println("2. 添加商品信息");
            System.out.println("3. 修改商品信息");
            System.out.println("4. 删除商品信息");
            System.out.println("5. 查询商品信息");
            System.out.println("6. 返回上层菜单");
            System.out.print("请输入您的选择: ");
    
            int choice = scanner.nextInt();
            scanner.nextLine();
    
            switch (choice) {
                case 1:
                    listAllProducts();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    searchProduct();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("无效的选择，请重试。");
            }
        }
    }
    
    private void listAllProducts() {
        // 连接数据库并从 Products 表中获取所有商品信息
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM Products")) {
    
            System.out.println("所有商品信息:");
            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String productName = resultSet.getString("name");
                double productPrice = resultSet.getDouble("price");
                // 输出商品信息
                System.out.println("ID: " + productId + ", 名称: " + productName + ", 价格: " + productPrice);
            }
        } catch (SQLException e) {
            System.out.println("获取商品信息失败: " + e.getMessage());
        }
    }
    
    private void addProduct() {
        System.out.print("请输入新商品的名称: ");
        String productName = scanner.nextLine();
    
        System.out.print("请输入新商品的价格: ");
        double productPrice = scanner.nextDouble();
        scanner.nextLine();
    
        // 连接数据库并将新商品添加到 Products 表
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO Products (name, price) VALUES (?, ?)")) {
            statement.setString(1, productName);
            statement.setDouble(2, productPrice);
            int rowsAffected = statement.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("商品添加成功！");
            } else {
                System.out.println("商品添加失败，请重试。");
            }
        } catch (SQLException e) {
            System.out.println("添加商品失败: " + e.getMessage());
        }
    }
    
    private void updateProduct() {
        System.out.print("请输入要修改的商品ID: ");
        int productIdToUpdate = scanner.nextInt();
        scanner.nextLine();
    
        System.out.print("请输入新商品的名称: ");
        String newProductName = scanner.nextLine();
    
        System.out.print("请输入新商品的价格: ");
        double newProductPrice = scanner.nextDouble();
        scanner.nextLine();
    
        // 连接数据库并更新指定商品信息
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement("UPDATE Products SET name = ?, price = ? WHERE id = ?")) {
            statement.setString(1, newProductName);
            statement.setDouble(2, newProductPrice);
            statement.setInt(3, productIdToUpdate);
            int rowsAffected = statement.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("商品信息修改成功！");
            } else {
                System.out.println("找不到指定商品，请检查ID。");
            }
        } catch (SQLException e) {
            System.out.println("修改商品信息失败: " + e.getMessage());
        }
    }
    
    private void deleteProduct() {
        System.out.print("请输入要删除的商品ID: ");
        int productIdToDelete = scanner.nextInt();
        scanner.nextLine();
    
        // 连接数据库并删除指定商品
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement("DELETE FROM Products WHERE id = ?")) {
            statement.setInt(1, productIdToDelete);
            int rowsAffected = statement.executeUpdate();
    
            if (rowsAffected > 0) {
                System.out.println("商品删除成功！");
            } else {
                System.out.println("找不到指定商品，请检查ID。");
            }
        } catch (SQLException e) {
            System.out.println("删除商品失败: " + e.getMessage());
        }
    }
    
    private void searchProduct() {
        System.out.print("请输入要查询的商品名称: ");
        String productNameToSearch = scanner.nextLine();
    
        // 连接数据库并查询指定商品
        try (Connection connection = DriverManager.getConnection(DB_URL);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM Products WHERE name = ?")) {
            statement.setString(1, productNameToSearch);
            ResultSet resultSet = statement.executeQuery();
    
            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String productName = resultSet.getString("name");
                double productPrice = resultSet.getDouble("price");
                // 输出商品信息
                System.out.println("ID: " + productId + ", 名称: " + productName + ", 价格: " + productPrice);
            }
        } catch (SQLException e) {
            System.out.println("查询商品失败: " + e.getMessage());
        }
    }
    
}
