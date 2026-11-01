package minibank;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import minibank.model.Account;
import minibank.model.BankException;
import minibank.repo.AccountRepository;
import minibank.service.BankService;

public class Main {
    private static final Path DATA_FILE = Paths.get("accounts.dat");
    private static final Path LOGS_DIR = Paths.get("logs");

    public static void main(String[] args) throws Exception {
        BankService bank = new BankService(new AccountRepository(), LOGS_DIR);
        if (Files.exists(DATA_FILE)) {
            System.out.println("Loaded " + bank.load(DATA_FILE)
                    + " saved account(s).");
        }
        Scanner in = new Scanner(System.in);
        boolean running = true;
        while (running) {
            printMenu();
            String choice = in.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> {
                        String owner = ask(in, "Owner name: ");
                        double opening = Double.parseDouble(
                                ask(in, "Opening balance: "));
                        Account a = bank.openAccount(owner, opening);
                        System.out.println("Opened " + a);
                    }
                    case "2" -> {
                        bank.deposit(num(in, "Account no: "),
                                Double.parseDouble(ask(in, "Amount: ")));
                        System.out.println("Deposit successful.");
                    }
                    case "3" -> {
                        bank.withdraw(num(in, "Account no: "),
                                Double.parseDouble(ask(in, "Amount: ")));
                        System.out.println("Withdrawal successful.");
                    }
                    case "4" -> {
                        bank.transfer(num(in, "From account: "),
                                num(in, "To account: "),
                                Double.parseDouble(ask(in, "Amount: ")));
                        System.out.println("Transfer successful.");
                    }
                    case "5" -> bank.statement(num(in, "Account no: "))
                            .forEach(System.out::println);
                    case "6" -> bank.listAccounts()
                            .forEach(System.out::println);
                    case "7" -> {
                        int no = num(in, "Account no: ");
                        bank.processBatch(no, 1000, 1.0);
                        System.out.println("Batch done. " + bank.listAccounts()
                                .stream().filter(a -> a.getAccountNumber() == no)
                                .findFirst().get());
                    }
                    case "8" -> System.out.println(bank.report());
                    case "9" -> {
                        String text = ask(in, "Owner name contains: ");
                        bank.searchByOwner(text).forEach(System.out::println);
                    }
                    case "0" -> {
                        bank.save(DATA_FILE);
                        System.out.println("Data saved. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            } catch (BankException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Error: please enter a valid number.");
            } catch (IOException e) {
                System.out.println("I/O error: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n===== MiniBank =====");
        System.out.println("1. Open account      2. Deposit");
        System.out.println("3. Withdraw          4. Transfer");
        System.out.println("5. Statement         6. List accounts");
        System.out.println("7. Batch (1000 deposits on thread pool)");
        System.out.println("8. End-of-day report 9. Search by owner");
        System.out.println("0. Save and exit");
        System.out.print("Choice: ");
    }

    private static String ask(Scanner in, String prompt) {
        System.out.print(prompt);
        return in.nextLine();
    }

    private static int num(Scanner in, String prompt) {
        return Integer.parseInt(ask(in, prompt).trim());
    }
}
