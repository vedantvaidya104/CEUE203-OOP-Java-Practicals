package discount;

import java.util.List;
import java.util.Scanner;

public class DiscountApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Double> prices = List.of(1000.0, 2500.0, 499.0);

        System.out.println("1. 10% off  2. Flat Rs.100 off  3. Buy more, save 20%");
        System.out.print("Pick a rule: ");
        int choice = sc.nextInt();

        DiscountRule rule = switch (choice) {
            case 1  -> p -> p * 0.90;
            case 2  -> p -> Math.max(0, p - 100);
            default -> p -> p * 0.80;
        };

        for (double p : prices) {
            System.out.println("Rs. " + p + " -> Rs. " + rule.apply(p));
        }
        sc.close();
    }
}
