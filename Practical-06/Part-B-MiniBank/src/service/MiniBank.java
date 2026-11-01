package service;

import static util.Validator.*;

import java.util.Scanner;
import model.*;

public class MiniBank {
    public static void main(String[] args) {
        BankInfo bank = new BankInfo("MiniBank", "CSPIT Main Branch");
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");

        Account savings = new SavingsAccount(5000, 1000);
        Account current = new CurrentAccount(2000, 5000);
        Account fd = new FixedDepositAccount(10000, false);

        // WithdrawRule 1: anonymous class
        WithdrawRule anonRule = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return account.canWithdraw(amount);
            }
        };
        // WithdrawRule 2: lambda expression
        WithdrawRule lambdaRule = (account, amount) -> account.canWithdraw(amount);

        System.out.println("Anonymous rule, Savings withdraw 3000: " + anonRule.allow(savings, 3000));
        System.out.println("Lambda rule,    Savings withdraw 3000: " + lambdaRule.allow(savings, 3000));
        System.out.println("Anonymous rule, FD withdraw 500      : " + anonRule.allow(fd, 500));
        System.out.println("Lambda rule,    FD withdraw 500      : " + lambdaRule.allow(fd, 500));

        System.out.println("Savings yearly interest  : " + savings.yearlyInterest());
        System.out.println("Savings monthly interest : "
            + String.format("%.2f", savings.monthlyInterest()));
        System.out.println("Current yearly interest  : " + current.yearlyInterest());
        System.out.println("Savings is Premium? " + (savings instanceof Premium));
        System.out.println("Current is Premium? " + (current instanceof Premium));

        Scanner sc = new Scanner(System.in);
        MenuOption[] options = MenuOption.values();
        boolean running = true;
        while (running) {
            System.out.println("\n--- MENU ---");
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            if (!isValidChoice(choice, options.length)) {
                System.out.println("Invalid choice.");
                continue;
            }
            MenuOption selected = options[choice - 1];
            String message = switch (selected) {
                case OPEN_ACCOUNT -> "Open Account - to be implemented in a later lab";
                case DEPOSIT      -> "Deposit - to be implemented in a later lab";
                case WITHDRAW     -> "Withdraw - to be implemented in a later lab";
                case TRANSFER     -> "Transfer - to be implemented in a later lab";
                case EXIT         -> { running = false; yield "Goodbye!"; }
            };
            System.out.println(message);
        }
        sc.close();
    }
}
