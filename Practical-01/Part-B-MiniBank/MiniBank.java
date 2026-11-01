import java.util.Scanner;

public class MiniBank {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankInfo bank = new BankInfo("MiniBank", "CSPIT Main Branch");
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");

        MenuOption[] options = MenuOption.values();
        boolean running = true;

        while (running) {
            System.out.println("\n--- MENU ---");
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            if (choice < 1 || choice > options.length) {
                System.out.println("Invalid choice. Please enter 1-" + options.length + ".");
                continue;
            }

            MenuOption selected = options[choice - 1];
            String message = switch (selected) {
                case OPEN_ACCOUNT  -> "Open Account - to be implemented in a later lab";
                case DEPOSIT       -> "Deposit - to be implemented in a later lab";
                case WITHDRAW      -> "Withdraw - to be implemented in a later lab";
                case TRANSFER      -> "Transfer - to be implemented in a later lab";
                case WORKING_HOURS -> "Working hours: Mon-Fri 10:00-16:00, Sat 10:00-13:00";
                case EXIT          -> {
                    running = false;
                    yield "Thank you for using MiniBank. Goodbye!";
                }
            };
            System.out.println(message);
        }
        sc.close();
    }
}

record BankInfo(String name, String branch) { }

enum MenuOption {
    OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, WORKING_HOURS, EXIT
}
