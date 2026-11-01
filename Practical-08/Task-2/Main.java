import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class OutOfStockException extends Exception {

    private int shortfall;

    public OutOfStockException(String message, int shortfall) {
        super(message);
        this.shortfall = shortfall;
    }

    public int getShortfall() {
        return shortfall;
    }
}

class InvalidQuantityException extends Exception {

    public InvalidQuantityException(String message) {
        super(message);
    }
}

class Warehouse {

    private Map<String, Integer> stock;

    public Warehouse() {
        stock = new HashMap<>();
    }

    public void addItem(String item, int quantity) {
        stock.put(item, quantity);
    }

    public void issue(String item, int qty)
            throws OutOfStockException, InvalidQuantityException {

        if (qty <= 0) {
            throw new InvalidQuantityException(
                "Quantity must be greater than 0."
            );
        }

        if (!stock.containsKey(item)) {
            throw new OutOfStockException(
                "Item '" + item + "' is not available.",
                qty
            );
        }

        int available = stock.get(item);

        if (qty > available) {

            int shortfall = qty - available;

            throw new OutOfStockException(
                "Not enough stock for '" + item + "'.",
                shortfall
            );
        }

        stock.put(item, available - qty);

        System.out.println(
            "Successfully issued " + qty + " " + item + "(s)."
        );

        System.out.println(
            "Remaining stock: " + (available - qty)
        );
    }

    public void displayStock() {

        System.out.println("\n--- Current Stock ---");

        for (Map.Entry<String, Integer> entry : stock.entrySet()) {

            System.out.println(
                entry.getKey() + " : " + entry.getValue()
            );
        }
    }
}

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Warehouse warehouse = new Warehouse();

        System.out.print("Enter number of different items: ");
        int n = sc.nextInt();

        System.out.println("\nEnter item name and quantity:");

        for (int i = 0; i < n; i++) {

            System.out.print("Item " + (i + 1) + " name: ");
            String item = sc.next();

            System.out.print("Quantity: ");
            int quantity = sc.nextInt();

            warehouse.addItem(item, quantity);
        }

        warehouse.displayStock();

        System.out.print("\nEnter number of requests: ");
        int requests = sc.nextInt();

        for (int i = 0; i < requests; i++) {

            System.out.println("\n--- Request " + (i + 1) + " ---");

            System.out.print("Enter item: ");
            String item = sc.next();

            System.out.print("Enter quantity: ");
            int qty = sc.nextInt();

            try {

                warehouse.issue(item, qty);

            } catch (OutOfStockException e) {

                System.out.println(
                    "Out of stock: " + e.getMessage()
                );

                System.out.println(
                    "Shortfall = " + e.getShortfall()
                );

            } catch (InvalidQuantityException e) {

                System.out.println(
                    "Invalid quantity: " + e.getMessage()
                );
            }
        }

        warehouse.displayStock();

        sc.close();
    }
}
