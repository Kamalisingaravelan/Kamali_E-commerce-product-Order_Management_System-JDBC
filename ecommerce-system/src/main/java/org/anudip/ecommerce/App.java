package org.anudip.ecommerce;

import org.anudip.ecommerce.model.*;
import org.anudip.ecommerce.service.*;
import org.anudip.ecommerce.util.ConfigUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

public class App {
    private static final Logger LOGGER = Logger.getLogger(App.class.getName());
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        UserService userService = new UserService();
        ProductService productService = new ProductService();
        OrderService orderService = new OrderService();
        CartService cartService = new CartService();

        LOGGER.info("E-commerce CLI demo started.");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1) Register user");
            System.out.println("2) Add product");
            System.out.println("3) List products");
            System.out.println("4) Add to cart");
            System.out.println("5) View cart");
            System.out.println("6) Remove from cart");
            System.out.println("7) List my orders");
            System.out.println("8) Update product");
            System.out.println("9) Delete product");
            System.out.println("10) View total revenue (admin)");
            System.out.println("11) View top selling products");
            System.out.println("12) Exit");
            System.out.print("> ");

            String opt = scanner.nextLine().trim();
            try {
                switch (opt) {
                    case "1": // Register user
                        System.out.print("Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        User user = new User();
                        user.setName(name);
                        user.setEmail(email);
                        int uid = userService.register(user);
                        if (uid > 0) {
                            System.out.println("✅ Registered. Your userId = " + uid);
                        } else {
                            System.out.println("❌ Failed to register user.");
                        }
                        break;

                    case "2": // Add product
                        System.out.print("Product name: ");
                        String pname = scanner.nextLine();
                        System.out.print("Price (e.g. 1500.00): ");
                        BigDecimal price = new BigDecimal(scanner.nextLine().trim());
                        System.out.print("Stock (int): ");
                        int stock = Integer.parseInt(scanner.nextLine().trim());
                        Product p = new Product();
                        p.setName(pname);
                        p.setPrice(price);
                        p.setStock(stock);
                        int pid = productService.add(p);
                        if (pid > 0) System.out.println("✅ Product added with id: " + pid);
                        else System.out.println("❌ Failed to add product.");
                        break;

                    case "3": // List products
                        List<Product> products = productService.listAll();
                        if (products.isEmpty()) {
                            System.out.println("⚠ No products available!");
                        } else {
                            System.out.println("Available products:");
                            products.forEach(System.out::println);
                        }
                        break;

                    case "4": // Add to cart
                        System.out.println("Do you have an account?");
                        System.out.println("1) Register new user");
                        System.out.println("2) Use existing userId");
                        System.out.print("> ");
                        String choice = scanner.nextLine().trim();

                        int currentUserId;
                        if ("1".equals(choice)) {
                            System.out.print("Enter your name: ");
                            String nname = scanner.nextLine();
                            System.out.print("Enter your email: ");
                            String nemail = scanner.nextLine();
                            User newUser = new User();
                            newUser.setName(nname);
                            newUser.setEmail(nemail);
                            currentUserId = userService.register(newUser);
                            if (currentUserId <= 0) {
                                System.out.println("❌ Registration failed.");
                                break;
                            }
                            System.out.println("✅ Registered. Your userId = " + currentUserId);
                        } else if ("2".equals(choice)) {
                            System.out.print("Enter your userId: ");
                            currentUserId = Integer.parseInt(scanner.nextLine().trim());
                            User existing = userService.getById(currentUserId);
                            if (existing == null) {
                                System.out.println("❌ User not found. Please register first.");
                                break;
                            }
                            System.out.println("✅ Welcome back, " + existing.getName() + "!");
                        } else {
                            System.out.println("❌ Invalid choice.");
                            break;
                        }

                        System.out.print("Enter product ID: ");
                        int addPid = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter quantity: ");
                        int addQty = Integer.parseInt(scanner.nextLine().trim());

                        Product prodToAdd = productService.getById(addPid);
                        if (prodToAdd == null) {
                            System.out.println("❌ Product not found.");
                            break;
                        }
                        if (prodToAdd.getStock() < addQty) {
                            System.out.println("⚠ Not enough stock. Available: " + prodToAdd.getStock());
                            break;
                        }
                        cartService.addToCart(currentUserId, addPid, addQty);
                        System.out.println("✅ Added to cart.");
                        break;

                    case "5": // View cart
                        System.out.print("Enter your userId: ");
                        int cartUid = Integer.parseInt(scanner.nextLine().trim());
                        User cartUser = userService.getById(cartUid);
                        if (cartUser == null) {
                            System.out.println("❌ User not found.");
                            break;
                        }
                        List<CartItem> cartItems = cartService.viewCart(cartUid);
                        if (cartItems.isEmpty()) {
                            System.out.println("⚠ Your cart is empty!");
                        } else {
                            System.out.println(cartUser.getName() + "'s cart items:");
                            BigDecimal cartTotal = BigDecimal.ZERO;
                            for (CartItem ci : cartItems) {
                                Product pr = productService.getById(ci.getProductId());
                                if (pr == null) continue;
                                BigDecimal line = pr.getPrice().multiply(new BigDecimal(ci.getQuantity()));
                                System.out.println("Product: " + pr.getName() + " | qty: " + ci.getQuantity()
                                        + " | price: " + pr.getPrice() + " | line: " + line);
                                cartTotal = cartTotal.add(line);
                            }
                            System.out.println("Cart subtotal = " + cartTotal);
                        }
                        break;

                    case "6": // Remove from cart
                        System.out.print("Enter your userId: ");
                        int remUid = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter product ID to remove: ");
                        int remPid = Integer.parseInt(scanner.nextLine().trim());
                        boolean removed = cartService.removeFromCart(remUid, remPid);
                        if (removed) System.out.println("✅ Item removed.");
                        else System.out.println("❌ Item not found in your cart.");
                        break;

                    case "7": // List orders
                        System.out.print("Enter your userId: ");
                        int ordUid = Integer.parseInt(scanner.nextLine().trim());
                        User ordUser = userService.getById(ordUid);
                        if (ordUser == null) {
                            System.out.println("❌ User not found.");
                            break;
                        }
                        List<Order> myOrders = orderService.getByUser(ordUid);
                        if (myOrders.isEmpty()) System.out.println("⚠ You have no orders.");
                        else myOrders.forEach(System.out::println);
                        break;

                    case "8": // Update product
                        System.out.print("Enter product ID to update: ");
                        int updPid = Integer.parseInt(scanner.nextLine().trim());
                        Product exist = productService.getById(updPid);
                        if (exist == null) {
                            System.out.println("❌ Product not found.");
                            break;
                        }
                        System.out.print("New name (" + exist.getName() + "): ");
                        String newName = scanner.nextLine();
                        if (!newName.isEmpty()) exist.setName(newName);
                        System.out.print("New price (" + exist.getPrice() + "): ");
                        String newPrice = scanner.nextLine();
                        if (!newPrice.isEmpty()) exist.setPrice(new BigDecimal(newPrice));
                        System.out.print("New stock (" + exist.getStock() + "): ");
                        String newStock = scanner.nextLine();
                        if (!newStock.isEmpty()) exist.setStock(Integer.parseInt(newStock));
                        boolean ok = productService.update(exist);
                        if (ok) System.out.println("✅ Product updated.");
                        else System.out.println("❌ Update failed.");
                        break;

                    case "9": // Delete product
                        System.out.print("Enter product ID to delete: ");
                        int delPid = Integer.parseInt(scanner.nextLine().trim());
                        boolean delOk = productService.delete(delPid);
                        if (delOk) System.out.println("✅ Product deleted.");
                        else System.out.println("❌ Delete failed.");
                        break;

                    case "10": // Total revenue
                        System.out.print("Admin password: ");
                        String pass = scanner.nextLine().trim();

                        String adminPass = ConfigUtil.getProperty("admin.password");
                        if (!adminPass.equals(pass)) {
                            System.out.println("❌ Unauthorized.");
                            break;
                        }

                        BigDecimal revenue = orderService.getTotalRevenue();
                        System.out.println("💰 Total revenue = " + revenue);
                        break;


                    case "11": // Top sellers
                        System.out.print("Top how many products? ");
                        int n = Integer.parseInt(scanner.nextLine().trim());
                        if (n <= 0) n = 5;
                        List<int[]> top = orderService.getTopSellingProducts(n);
                        if (top.isEmpty()) {
                            System.out.println("⚠ No sales data yet.");
                        } else {
                            System.out.println("Top selling products (productId -> qtySold):");
                            for (int[] row : top) {
                                Product pr = productService.getById(row[0]);
                                String nameTop = pr != null ? pr.getName() : "product#" + row[0];
                                System.out.println(nameTop + " (id=" + row[0] + ") -> " + row[1]);
                            }
                        }
                        break;

                    case "12": // Exit
                        System.out.println("Thanks for your visiting, Welcome again!...");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
