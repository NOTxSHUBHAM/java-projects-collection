import java.util.Scanner;

public class CoffeeShop {
    static final double TAX_RATE = 0.08;
    static final double DISCOUNT_THRESHOLD = 500;
    static final double DISCOUNT_RATE = 0.10;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] items  = {"Espresso", "Cappuccino", "Latte", "Cold Coffee", "Tea"};
        double[] prices = {80, 120, 150, 100, 60};

        System.out.println("====== Welcome to Java Coffee Shop ======");
        System.out.println("Menu:");
        for (int i = 0; i < items.length; i++)
            System.out.printf("%d. %-15s Rs. %.2f%n", i + 1, items[i], prices[i]);

        double subtotal = 0;
        System.out.print("\nHow many items do you want to order? ");
        int n = sc.nextInt();

        System.out.println("\n--- Your Order ---");
        for (int i = 0; i < n; i++) {
            System.out.print("Enter item number: ");
            int choice = sc.nextInt() - 1;
            System.out.print("Enter quantity: ");
            int qty = sc.nextInt();
            double cost = prices[choice] * qty;
            subtotal += cost;
            System.out.printf("  %s x%d = Rs. %.2f%n", items[choice], qty, cost);
        }

        double discount = subtotal >= DISCOUNT_THRESHOLD ? subtotal * DISCOUNT_RATE : 0;
        double afterDiscount = subtotal - discount;
        double tax   = afterDiscount * TAX_RATE;
        double total = afterDiscount + tax;

        System.out.println("\n========== RECEIPT ==========");
        System.out.printf("Subtotal  : Rs. %.2f%n", subtotal);
        System.out.printf("Discount  : Rs. %.2f%n", discount);
        System.out.printf("Tax (8%%) : Rs. %.2f%n", tax);
        System.out.printf("TOTAL     : Rs. %.2f%n", total);
        System.out.println("Thank you for visiting!");
        sc.close();
    }
}
