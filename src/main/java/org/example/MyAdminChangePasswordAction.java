package org.example;
import java.util.Scanner;

public class MyAdminChangePasswordAction implements MyAction {

    private static final String ACTION_NAME = "change_password";

    private Scanner scanner;
    private MyAdminManager adminManager;

    public MyAdminChangePasswordAction(Scanner scanner, MyAdminManager adminManager) {
        this.scanner = scanner;
        this.adminManager = adminManager;
    }

    @Override
    public String getActionName() {
        return MyAdminChangePasswordAction.ACTION_NAME;
    }

    @Override
    public void run(String[] args) {
        System.out.println("现在您在管理员密码修改子菜单中.");

        while (true) {
            System.out.print("请输入当前密码：");
            String currentPassword = scanner.nextLine();

            System.out.print("请输入新密码：");
            String newPassword = scanner.nextLine();

            boolean success = adminManager.changePassword(currentPassword, newPassword);

            if (success) {
                System.out.println("管理员密码修改成功！");
                break;
            } else {
                System.out.println("管理员密码修改失败，请重试。");
            }
        }
    }
}
