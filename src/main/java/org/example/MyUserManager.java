package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MyUserManager {
    private Map<String, Integer> shoppingCart = new HashMap<>(); // 商品编号 -> 数量
    private Scanner scanner;
    private MyUserRegisterAction userRegisterAction;
    private MyUserLoginAction userLoginAction;

    public MyUserManager(Scanner scanner) {
        this.scanner = scanner;
        this.userRegisterAction = new MyUserRegisterAction(scanner, this);
        this.userLoginAction = new MyUserLoginAction(scanner, this);
    }

    public boolean registerUser(String username, String password) {
        MyUser user = new MyUser();
        return user.registerUser(username, password);
    }

    public boolean login(String username, String password) {
        MyUser user = new MyUser();
        return user.login(username, password);
    }

    public void managePassword() {
        System.out.println("密码管理菜单:");
        System.out.println("1. 修改密码");
        System.out.println("2. 返回上层菜单");
        System.out.print("请输入您的选择: ");
    
        int choice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
    
        switch (choice) {
            case 1:
                changePassword();
                break;
            case 2:
                return;
            default:
                System.out.println("无效的选择，请重试。");
        }
    }
    
    private void changePassword() {
        System.out.print("请输入旧密码: ");
        String oldPassword = scanner.nextLine();
    
        // 在数据库中验证旧密码是否正确（实际操作数据库的代码）
    
        System.out.print("请输入新密码: ");
        String newPassword = scanner.nextLine();
    
        // 更新用户密码（实际操作数据库的代码）
    
        System.out.println("密码修改成功！");
    }
    

    


    

    public void userMenu() {
        while (true) {
            System.out.println("用户手册:");
            System.out.println("1. 注册");
            System.out.println("2. 登录");
            System.out.println("3. 密码管理");
            System.out.println("4. 购物");
            System.out.println("5. 退出登录");
            System.out.print("请输入您的选择: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    userRegisterAction.run(null);
                    break;
                case 2:
                    userLoginAction.run(null);
                    break;
                case 3:
                    managePassword();
                    break;
                case 4:
                    shopping();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    public void shopping() {
        System.out.println("欢迎进入购物系统！");
        System.out.println("商品列表:");
        // 显示商品列表（实际操作数据库或数据结构的代码）

        while (true) {
            System.out.println("1. 将商品加入购物车");
            System.out.println("2. 将商品从购物车中移除");
            System.out.println("3. 修改购物车中的商品");
            System.out.println("4. 付账");
            System.out.println("5. 查看购物历史");
            System.out.println("6. 返回上层菜单");
            System.out.print("请输入您的选择: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            switch (choice) {
                case 1:
                    addToCart();
                    break;
                case 2:
                    removeFromCart();
                    break;
                case 3:
                    modifyCart();
                    break;
                case 4:
                    checkout();
                    break;
                case 5:
                    viewPurchaseHistory();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("无效的选择，请重试。");
            }
        }
    }

    private void addToCart() {
        System.out.print("请输入商品编号: ");
        String productCode = scanner.nextLine();

        System.out.print("请输入数量: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        if (quantity <= 0) {
            System.out.println("数量必须大于0。");
            return;
        }

        // 将商品添加到购物车
        if (shoppingCart.containsKey(productCode)) {
            shoppingCart.put(productCode, shoppingCart.get(productCode) + quantity);
        } else {
            shoppingCart.put(productCode, quantity);
        }
        System.out.println("商品已添加到购物车。");
    }

    private void removeFromCart() {
        System.out.print("请输入要移除的商品编号: ");
        String productCode = scanner.nextLine();

        if (shoppingCart.containsKey(productCode)) {
            System.out.print("是否确认移除该商品？(Y/N): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("Y")) {
                shoppingCart.remove(productCode);
                System.out.println("商品已从购物车中移除。");
            } else {
                System.out.println("取消移除操作。");
            }
        } else {
            System.out.println("购物车中不存在该商品。");
        }
    }

    private void modifyCart() {
        System.out.print("请输入要修改数量的商品编号: ");
        String productCode = scanner.nextLine();

        if (shoppingCart.containsKey(productCode)) {
            System.out.print("请输入新的数量: ");
            int newQuantity = scanner.nextInt();
            scanner.nextLine(); // 消耗换行符

            if (newQuantity <= 0) {
                System.out.println("数量必须大于0。");
                return;
            }

            shoppingCart.put(productCode, newQuantity);
            System.out.println("购物车中的商品数量已修改。");
        } else {
            System.out.println("购物车中不存在该商品。");
        }
    }

    private void checkout() {
        // 实际付账操作，修改相应的商品数量和购物历史记录
        // 这里只是一个简化示例，没有实际付账逻辑
    
        // 用户选择支付方式
        System.out.println("请选择支付方式:");
        System.out.println("1. 微信支付");
        System.out.println("2. 支付宝支付");
        System.out.print("请输入您的选择: ");
        int paymentChoice = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符
    
        switch (paymentChoice) {
            case 1:
                weChatPayment();
                break;
            case 2:
                aliPayPayment();
                break;
            default:
                System.out.println("无效的选择，请重试。");
                return;
        }
    
        // 模拟付账后，清空购物车
        shoppingCart.clear();
        System.out.println("付账成功！");
    }
    
    private void weChatPayment() {
        // 模拟微信支付操作，可以在此处实现相关逻辑
        System.out.println("正在使用微信支付...");
    }
    
    private void aliPayPayment() {
        // 模拟支付宝支付操作，可以在此处实现相关逻辑
        System.out.println("正在使用支付宝支付...");
    }
    

    private void viewPurchaseHistory() {
        // 查询购物历史记录并显示时间和购买的商品清单
        // 这里只是一个简化示例，没有实际购物历史查询逻辑
        System.out.println("购物历史记录：");
        for (Map.Entry<String, Integer> entry : shoppingCart.entrySet()) {
            String productCode = entry.getKey();
            int quantity = entry.getValue();
            System.out.println("商品编号: " + productCode + ", 数量: " + quantity);
        }
    }
}
