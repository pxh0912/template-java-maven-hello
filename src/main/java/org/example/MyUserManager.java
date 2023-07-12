package org.example;

import java.util.Scanner;

public class MyUserManager {
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
        // Password management logic
    }

    public void shopping() {
        // Shopping logic
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
}
