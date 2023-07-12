package org.example;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        System.out.println("欢迎使用购物管理系统!");

        DatabaseInitializer databaseInitializer = new DatabaseInitializer();
        databaseInitializer.initializeDatabase();


        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("请选择您的身份:");
            System.out.println("1. 管理员");
            System.out.println("2. 用户");
            System.out.println("3. 退出");
            System.out.print("请输入您的操作: ");

            int roleChoice = scanner.nextInt();
            scanner.nextLine();

            if (roleChoice == 1) {
                MyAdminManager adminManager = new MyAdminManager(scanner);
                adminManager.adminMenu();
            } else if (roleChoice == 2) {
                MyUserManager userManager = new MyUserManager(scanner);
                userManager.userMenu();
            } else if (roleChoice == 3) {
                break;
            } else {
                System.out.println("请重新输入您的操作");
            }
        }

        scanner.close();
        System.out.println("欢迎您下次使用!");
    }
}
