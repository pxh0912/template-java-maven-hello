package org.example;
import java.util.Scanner;

public class MyAdminLoginAction implements MyAction {

    private static final String ACTION_NAME = "login";

    private Scanner scanner;
    private MyAdminManager adminManager;

    public MyAdminLoginAction(Scanner scanner, MyAdminManager adminManager) {
        this.scanner = scanner;
        this.adminManager = adminManager;
    }

    @Override
    public String getActionName() {
        return MyAdminLoginAction.ACTION_NAME;
    }

    @Override
    public void run(String[] args) {
        System.out.println("现在您在管理员登录子菜单中.");

        while (true) {
            System.out.print("用户名：");
            String username = scanner.nextLine();

            System.out.print("密码：");
            String password = scanner.nextLine();

            boolean success = adminManager.login(username, password);

            if (success) {
                System.out.println("管理员登录成功！");
                break;
            } else {
                System.out.println("管理员登录失败，请重试。");
            }
        }
    }

}

