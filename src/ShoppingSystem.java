import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class ShoppingSystem {
    private static final String USER_FILE = "users.txt";
    private static final String PRODUCT_FILE = "products.txt";
    private static final String HISTORY_FILE = "history.txt";
    private static List<User> users;
    private static List<Product> products;
    private static List<ShoppingHistory> history;
    private static User currentUser;
    private static Scanner scanner;

    public static void main(String[] args) {
        users = new ArrayList<>();
        products = new ArrayList<>();
        history = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadUsers();
        loadProducts();
        loadHistory();
        System.out.println("欢迎来到购物管理系统!");
        while (true) {
            if (currentUser == null) {
                Menu();
            } else {
                if (currentUser.isAdmin()) {
                    adminManagement();
                } else {
                    userMenu();
                }
            }
        }
    }
    private static void Menu() {
        System.out.println("请选择您的身份:");
        System.out.println("1. 管理员");
        System.out.println("2. 用户");
        System.out.println("3. 退出");
        System.out.print("请输入您的选择: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                adminLogin();
                break;
            case 2:
                userMenu();
                break;
            case 3:
                saveData();
                System.out.println("感谢使用本购物管理系统，祝您生活愉快!");
                System.exit(0); // 退出程序
            default:
                System.out.println("无效选择，请重试.");
        }
    }
    private static void adminManagement() {
        System.out.println("管理员 " + currentUser.getUsername() + "，请选择操作:");
        System.out.println("1. 密码管理");
        System.out.println("2. 用户管理");
        System.out.println("3. 商品管理");
        System.out.println("4. 退出登录");
        System.out.print("请输入您的选择: ");
        int adminChoice = scanner.nextInt();
        scanner.nextLine();

        switch (adminChoice) {
            case 1:
                changePassword();
                break;
            case 2:
                customerManagement();
                break;
            case 3:
                productManagement();
                break;
            case 4:
                currentUser = null;
                System.out.println("退出成功.");
                break;
            default:
                System.out.println("无效选择，请重试.");
        }
    }
    private static void userMenu() {
        while (true) {
            System.out.println("用户菜单:");
            System.out.println("1. 注册");
            System.out.println("2. 登录");
            System.out.println("3. 退出");
            System.out.print("请输入您的选择: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    if (userLogin()) {
                        userSubMenu();
                    }
                    break;
                case 3:
                    return;
                default:
                    System.out.println("无效的选择，请重新输入");
            }
        }
    }
    private static void userSubMenu() {
        while (true) {
            System.out.println("用户操作菜单:");
            System.out.println("1. 密码管理");
            System.out.println("2. 购物");
            System.out.println("3. 退出登录");
            System.out.print("请输入您的选择: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1:
                    changeUserPassword();
                    break;
                case 2:
                    shopping();
                    break;
                case 3:
                    currentUser = null;
                    System.out.println("退出登录成功");
                    return;
                default:
                    System.out.println("无效的选择，请重新输入");
            }
        }
    }
    private static void adminLogin() {
        if (currentUser != null) {
            System.out.println("您已登录， " + currentUser.getUsername());
            return;
        }
        System.out.print("用户名: ");
        String username = scanner.nextLine();
        System.out.print("密码: ");
        String password = scanner.nextLine();
        // Check if the entered username and password match the default admin credentials
        if (username.equals("12345") && password.equals("ynuinfo#777")) {
            User adminUser = new User("12345", hashPassword("ynuinfo#777"));
            adminUser.setAdmin(true);
            currentUser = adminUser;
            System.out.println("欢迎 " + currentUser.getUsername());
            return;
        }
        // 如果不是默认的管理员账户，则检查用户列表
        for (User user : users) {
            if (user.getUsername().equals(username) && verifyPassword(password, user.getPassword())) {
                currentUser = user;
                System.out.println("登录成功， " + currentUser.getUsername());
                return;
            }
        }
        System.out.println("用户名或密码无效");
    }
    private static void changePassword() {
        System.out.print("请输入当前密码: ");
        String currentPassword = scanner.nextLine();
        if (!verifyPassword(currentPassword, currentUser.getPassword())) {
            System.out.println("密码错误！");
            return;
        }
        System.out.print("请输入新密码: ");
        String newPassword = scanner.nextLine();
        currentUser.setPassword(hashPassword(newPassword));
        System.out.println("密码修改成功.");
    }

    private static void customerManagement() {
        System.out.println("用户管理:");
        System.out.println("1. 列举所有用户");
        System.out.println("2. 删除用户");
        System.out.println("3. 查找用户");
        System.out.println("4. 返回主菜单");
        System.out.print("请输入您的选择: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                listCustomers();
                break;
            case 2:
                deleteCustomer();
                break;
            case 3:
                searchCustomer();
                break;
            case 4:
                break;
            default:
                System.out.println("无效选择，请重新输入");
        }
    }

    private static void listCustomers() {
        System.out.println("Customer Information:");
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-5s %-15s %-15s %-20s %-15s %-15s %-15s%n",
                "ID", "Username", "User Level", "Registration Time", "Total Expenditure", "Phone Number", "Email");
        for (User user : users) {
            if (!user.isAdmin()) {
                String userLevel = getUserLevel(user.getUsername());
                String registrationTime = getRegistrationTime(user.getUsername());
                double totalExpenditure = getTotalExpenditure(user.getUsername());
                String phoneNumber = getPhoneNumber(user.getUsername());
                String email = getEmail(user.getUsername());

                System.out.printf("%-5s %-15s %-15s %-20s %-15.2f %-15s %-15s%n",
                        user.getId(), user.getUsername(), userLevel, registrationTime, totalExpenditure, phoneNumber, email);
            }
        }
        System.out.println("-------------------------------------------------------");
    }

    private static String getUserLevel(String username) {
        return "Gold";
    }

    private static String getRegistrationTime(String username) {
        return "2023-08-27 10:00:00";
    }

    private static double getTotalExpenditure(String username) {
        return 1000.0;
    }

    private static String getPhoneNumber(String username) {
        return "123-456-7890";
    }

    private static String getEmail(String username) {
        return "user@example.com";
    }

    private static void deleteCustomer() {
        System.out.print("请输入要删除的顾客用户名: ");
        String username = scanner.nextLine();
        boolean found = false;
        for (User user : users) {
            if (!user.isAdmin() && user.getUsername().equals(username)) {
                found = true;
                System.out.println("找到用户:");
                System.out.println("用户名: " + user.getUsername());
                System.out.print("是否确认删除该用户信息？ (Y/N): ");
                String confirmation = scanner.nextLine();
                if (confirmation.equalsIgnoreCase("Y")) {
                    users.remove(user);
                    System.out.println("删除成功");
                } else {
                    System.out.println("删除操作已取消");
                }
                break;
            }
        }

        if (!found) {
            System.out.println("未找到该用户.");
        }
    }
    private static void searchCustomer() {
        System.out.print("请输入要查找的顾客用户名：");
        String username = scanner.nextLine();
        boolean found = false;

        for (User user : users) {
            if (!user.isAdmin() && user.getUsername().equals(username)) {
                System.out.println("找到用户:");
                System.out.println("用户名: " + user.getUsername());
                System.out.println("密码: " + user.getPassword());
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("未找到该用户.");
        }
    }

    private static void productManagement() {
        while (true) {
            System.out.println("商品管理菜单:");
            System.out.println("1. 列出所有商品");
            System.out.println("2. 添加商品");
            System.out.println("3. 修改商品");
            System.out.println("4. 删除商品");
            System.out.println("5. 查询商品");
            System.out.println("6. 返回主菜单");
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
                    modifyProduct();
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
                    System.out.println("无效的选择，请重新输入");
            }
        }
    }

    private static void listAllProducts() {
        System.out.println("所有商品信息:");
        for (Product product : products) {
            System.out.println("商品编号: " + product.getId());
            System.out.println("商品名称: " + product.getName());
            System.out.println("生产厂家: " + product.getManufacturer());
            System.out.println("生产日期: " + product.getProductionDate());
            System.out.println("型号: " + product.getModel());
            System.out.println("进货价: " + product.getPurchasePrice());
            System.out.println("零售价格: " + product.getRetailPrice());
            System.out.println("数量: " + product.getQuantity());
            System.out.println();
        }
    }

    private static void addProduct() {
        System.out.print("请输入商品名称: ");
        String name = scanner.nextLine();
        System.out.print("请输入生产厂家: ");
        String manufacturer = scanner.nextLine();
        System.out.print("请输入生产日期: ");
        String productionDate = scanner.nextLine();
        System.out.print("请输入型号: ");
        String model = scanner.nextLine();
        System.out.print("请输入进货价: ");
        double purchasePrice = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("请输入零售价格: ");
        double retailPrice = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("请输入数量: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        int id = generateProductId();
        Product product = new Product(id, name, manufacturer, productionDate, model, purchasePrice, retailPrice, quantity);
        products.add(product);
        System.out.println("商品添加成功，编号为 " + id);
    }

    private static void modifyProduct() {
        System.out.print("请输入要修改的商品的编号: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Product product = findProductById(id);
        if (product == null) {
            System.out.println("未找到该商品.");
            return;
        }

        System.out.print("请输入新的商品名称: ");
        String name = scanner.nextLine();
        System.out.print("请输入新的生产厂家: ");
        String manufacturer = scanner.nextLine();
        System.out.print("请输入新的生产日期: ");
        String productionDate = scanner.nextLine();
        System.out.print("请输入新的型号: ");
        String model = scanner.nextLine();
        System.out.print("请输入新的进货价: ");
        double purchasePrice = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("请输入新的零售价格: ");
        double retailPrice = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("请输入新的数量: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();
        product.setName(name);
        product.setManufacturer(manufacturer);
        product.setProductionDate(productionDate);
        product.setModel(model);
        product.setPurchasePrice(purchasePrice);
        product.setRetailPrice(retailPrice);
        product.setQuantity(quantity);
        System.out.println("商品修改成功");
    }

    private static void deleteProduct() {
        System.out.print("请输入要删除的商品的编号: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Product product = findProductById(id);
        if (product == null) {
            System.out.println("未找到该商品");
            return;
        }
        System.out.print("警告：删除后无法恢复，请确认是否继续删除？ (Y/N): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("Y")) {
            products.remove(product);
            System.out.println("商品删除成功");
        } else {
            System.out.println("删除操作已取消");
        }
    }

    private static void searchProduct() {
        System.out.println("查询商品信息:");
        System.out.println("1. 根据商品名称查询");
        System.out.println("2. 根据生产厂家查询");
        System.out.println("3. 根据零售价格查询");
        System.out.print("请选择查询方式 (1/2/3): ");
        int searchChoice = scanner.nextInt();
        scanner.nextLine();
        switch (searchChoice) {
            case 1:
                searchProductByName();
                break;
            case 2:
                searchProductByManufacturer();
                break;
            case 3:
                searchProductByRetailPrice();
                break;
            default:
                System.out.println("无效的选择，请重新输入");
        }
    }
    private static void searchProductByName() {
        System.out.print("请输入要查询的商品名称: ");
        String nameToSearch = scanner.nextLine();

        System.out.println("查询结果:");
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(nameToSearch)) {
                displayProductInfo(product);
            }
        }
    }

    private static void searchProductByManufacturer() {
        System.out.print("请输入要查询的生产厂家: ");
        String manufacturerToSearch = scanner.nextLine();
        System.out.println("查询结果:");
        for (Product product : products) {
            if (product.getManufacturer().equalsIgnoreCase(manufacturerToSearch)) {
                displayProductInfo(product);
            }
        }
    }

    private static void searchProductByRetailPrice() {
        System.out.print("请输入零售价格的下限: ");
        double minRetailPrice = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("请输入零售价格的上限: ");
        double maxRetailPrice = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("查询结果:");
        for (Product product : products) {
                product.getRetailPrice();

        }
    }

    private static void displayProductInfo(Product product) {
        System.out.println("商品编号: " + product.getId());
        System.out.println("商品名称: " + product.getName());
        System.out.println("生产厂家: " + product.getManufacturer());
        System.out.println("生产日期: " + product.getProductionDate());
        System.out.println("型号: " + product.getModel());
        System.out.println("进货价: " + product.getPurchasePrice());
        System.out.println("零售价格: " + product.getRetailPrice());
        System.out.println("数量: " + product.getQuantity());
        System.out.println();
    }
        private static void registerUser() {
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        if (isUsernameTaken(username)) {
            System.out.println("用户名已存在");
            return;
        }
        System.out.print("请输入密码");
        String password = scanner.nextLine();
        User user = new User(username, hashPassword(password));
        users.add(user);
        System.out.println("注册成功");
    }

    private static boolean isAdminSelected() {
        return currentUser == null;
    }
    private static boolean userLogin() {
        if (currentUser != null) {
            System.out.println("您已登录， " + currentUser.getUsername());
            return true;
        }
        System.out.print("用户名: ");
        String username = scanner.nextLine();
        System.out.print("密码: ");
        String password = scanner.nextLine();
        for (User user : users) {
            if (user.getUsername().equals(username) && verifyPassword(password, user.getPassword())) {
                currentUser = user;
                System.out.println("登录成功， " + currentUser.getUsername());
                return true;
            }
        }
        System.out.println("用户名或密码无效");
        return false;
    }

    private static void changeUserPassword() {
        System.out.print("请输入当前密码: ");
        String currentPassword = scanner.nextLine();
        if (!verifyPassword(currentPassword, currentUser.getPassword())) {
            System.out.println("当前密码错误");
            return;
        }
        System.out.print("请输入新密码: ");
        String newPassword = scanner.nextLine();
        currentUser.setPassword(hashPassword(newPassword));
        System.out.println("密码更换成功");
    }

    private static void shopping() {
        List<Product> cart = new ArrayList<>(); // Create a shopping cart to store selected products
        System.out.println("欢迎来到购物页面!");
        while (true) {
            System.out.println("请选择要购买的产品或输入 '0' 结束购物:");
            listProducts(); // 列出所有产品供用户选择
            System.out.print("请输入产品编号: ");
            int productId = scanner.nextInt();
            if (productId == 0) {
                // 用户选择结束购物，退出购物循环
                break;
            }
            Product selectedProduct = findProductById(productId);
            if (selectedProduct == null) {
                System.out.println("无效的产品编号，请重新选择.");
                continue;
            }
            System.out.println("您选择了产品: " + selectedProduct.getName());
            System.out.println("价格: " + selectedProduct.getPrice());
            System.out.println("库存: " + selectedProduct.getQuantity());
            System.out.print("请输入购买数量: ");
            int quantityToBuy = scanner.nextInt();
            if (quantityToBuy <= 0) {
                System.out.println("购买数量无效，请重新选择.");
                continue;
            }
            if (quantityToBuy > selectedProduct.getQuantity()) {
                System.out.println("库存不足，请重新选择购买数量.");
                continue;
            }
            double totalPrice = selectedProduct.getPrice() * quantityToBuy;
            // 更新产品库存
            selectedProduct.setQuantity(selectedProduct.getQuantity() - quantityToBuy);
            Product cartItem = new Product(selectedProduct.getId(), selectedProduct.getName(), selectedProduct.getPrice(), quantityToBuy);
            cart.add(cartItem);
            System.out.println("已加入购物车!");
            System.out.println("总价: " + totalPrice);
        }
        while (!cart.isEmpty()) {
            System.out.println("\n购物车操作:");
            System.out.println("1. 移除商品");
            System.out.println("2. 修改商品数量");
            System.out.println("3. 付账");
            System.out.println("4. 查看购物车");
            System.out.println("5. 返回主菜单");
            System.out.print("请输入您的选择: ");
            int cartChoice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            switch (cartChoice) {
                case 1:
                    removeProductFromCart(cart);
                    break;
                case 2:
                    modifyCartItem(cart);
                    break;
                case 3:
                    checkout(cart);
                    return; // Exit the shopping loop
                case 4:
                    displayCart(cart);
                    break;
                case 5:
                    return; // Exit the shopping loop and return to the main menu
                default:
                    System.out.println("无效的选择，请重新输入.");
            }
        }
    }

    private static void removeProductFromCart(List<Product> cart) {
        System.out.println("购物车中的商品:");
        displayCart(cart);
        System.out.print("请输入要移除的商品编号: ");
        int productIdToRemove = scanner.nextInt();
        scanner.nextLine();
        for (Product item : cart) {
            if (item.getId() == productIdToRemove) {
                System.out.print("确认移除商品 '" + item.getName() + "' (Y/N)? ");
                String confirmation = scanner.nextLine().trim().toLowerCase();
                if (confirmation.equals("y")) {
                    // Remove the product from the cart
                    cart.remove(item);
                    System.out.println("商品已移除.");
                    return;
                } else {
                    System.out.println("取消移除操作.");
                    return;
                }
            }
        }
        System.out.println("未找到指定的商品.");
    }

    private static void modifyCartItem(List<Product> cart) {
        System.out.println("购物车中的商品:");
        displayCart(cart);
        System.out.print("请输入要修改数量的商品编号: ");
        int productIdToModify = scanner.nextInt();
        scanner.nextLine();
        for (Product item : cart) {
            if (item.getId() == productIdToModify) {
                System.out.print("请输入新的数量 (0 则移除商品): ");
                int newQuantity = scanner.nextInt();
                scanner.nextLine();
                if (newQuantity < 0) {
                    System.out.println("购买数量无效，商品将被移除.");
                    cart.remove(item);
                } else if (newQuantity == 0) {
                    System.out.println("商品已被移除.");
                    cart.remove(item);
                } else {
                    item.setQuantity(newQuantity);
                    System.out.println("商品数量已修改.");
                }
                return;
            }
        }
        System.out.println("未找到指定的商品.");
    }

    private static void displayCart(List<Product> cart) {
        for (Product item : cart) {
            System.out.println("商品编号: " + item.getId());
            System.out.println("商品名称: " + item.getName());
            System.out.println("单价: " + item.getPrice());
            System.out.println("数量: " + item.getQuantity());
            System.out.println("小计: " + item.getPrice() * item.getQuantity());
            System.out.println();
        }
    }

    private static void checkout(List<Product> cart) {
        System.out.println("模拟支付操作...");
        System.out.println("支付成功!");
        cart.clear();
    }

    private static void listProducts() {
    }
    private static boolean isUsernameTaken(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
    private static Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }
    private static int generateProductId() {
        int id = 1;
        for (Product product : products) {
            if (product.getId() >= id) {
                id = product.getId() + 1;
            }
        }
        return id;
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("错误的哈希密码");
            return null;
        }
    }

    private static boolean verifyPassword(String password, String hashedPassword) {
        String hashedInput = hashPassword(password);
        return hashedInput != null && hashedInput.equals(hashedPassword);
    }

    private static void loadUsers() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(USER_FILE));
            for (String line : lines) {
                String[] parts = line.split(",");
                String username = parts[0];
                String password = parts[1];
                boolean isAdmin = Boolean.parseBoolean(parts[2]);
                User user = new User(username, password);
                user.setAdmin(isAdmin);
                users.add(user);
            }
        } catch (IOException e) {
            System.out.println("Error loading user data. Starting with an empty user list.");
        }
    }

    private static void loadProducts() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(PRODUCT_FILE));
            for (String line : lines) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                double price = Double.parseDouble(parts[2]);
                int quantity = Integer.parseInt(parts[3]);

                Product product = new Product(id, name, price, quantity);
                products.add(product);
            }
        } catch (IOException e) {
            System.out.println("Error loading product data. Starting with an empty product list.");
        }
    }

    private static void loadHistory() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(HISTORY_FILE));

            for (String line : lines) {
                String[] parts = line.split(",");
                String username = parts[0];
                int productId = Integer.parseInt(parts[1]);
                int quantity = Integer.parseInt(parts[2]);

                ShoppingHistory shoppingHistory = new ShoppingHistory(username, productId, quantity);
                history.add(shoppingHistory);
            }
        } catch (IOException e) {
            System.out.println("Error loading history data. Starting with an empty history list.");
        }
    }

    private static void saveData() {
        try {
            FileWriter userWriter = new FileWriter(USER_FILE);
            FileWriter productWriter = new FileWriter(PRODUCT_FILE);
            FileWriter historyWriter = new FileWriter(HISTORY_FILE);
            for (User user : users) {
                String line = user.getUsername() + "," + user.getPassword() + "," + user.isAdmin() + "\n";
                userWriter.write(line);
            }
            for (Product product : products) {
                String line = product.getId() + "," + product.getName() + "," + product.getPrice() + "," + product.getQuantity() + "\n";
                productWriter.write(line);
            }
            for (ShoppingHistory shoppingHistory : history) {
                String line = shoppingHistory.getUsername() + "," + shoppingHistory.getProductId() + "," + shoppingHistory.getQuantity() + "\n";
                historyWriter.write(line);
            }
            userWriter.close();
            productWriter.close();
            historyWriter.close();
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }
}

class User {
    private String username;
    private String password;
    private boolean isAdmin;
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Object getId() {
        return null;
    }
}

class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
    public Product(int id, String name, String manufacturer, String productionDate, String model, double purchasePrice, double retailPrice, int quantity) {
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getManufacturer() {
        return null;
    }
    public String getModel() {
        return null;
    }
    public String getProductionDate() {
        return null;
    }
    public String getPurchasePrice() {
        return null;
    }
    public String getRetailPrice() {        return null;

    }
    public void setManufacturer(String manufacturer) {
    }

    public void setProductionDate(String productionDate) {
    }

    public void setModel(String model) {
    }
    public void setPurchasePrice(double purchasePrice) {
    }
    public void setRetailPrice(double retailPrice) {
    }
}

class ShoppingHistory {
    private String username;
    private int productId;
    private int quantity;
    public ShoppingHistory(String username, int productId, int quantity) {
        this.username = username;
        this.productId = productId;
        this.quantity = quantity;
    }
    public String getUsername() {
        return username;
    }
    public int getProductId() {
        return productId;
    }
    public int getQuantity() {
        return quantity;
    }
}