abstract class Account {
    long balance;

    Account(long balance) {
        this.balance = balance;
    }

    abstract double interestRate();

    abstract boolean canWithdraw(long amount);

    double monthlyInterest() {
        return (balance * interestRate() / 100.0) / 12.0;
    }
}

class SavingsAccount extends Account {
    long minBalance;

    SavingsAccount(long balance, long minBalance) {
        super(balance);
        this.minBalance = minBalance;
    }

    double interestRate() {
        return 4.0;
    }

    boolean canWithdraw(long amount) {
        return (balance - amount) >= minBalance;
    }
}

class CurrentAccount extends Account {
    long overdraftLimit;

    CurrentAccount(long balance, long overdraftLimit) {
        super(balance);
        this.overdraftLimit = overdraftLimit;
    }

    double interestRate() {
        return 0.0;
    }

    boolean canWithdraw(long amount) {
        return (balance - amount) >= -overdraftLimit;
    }
}

class FixedDepositAccount extends Account {
    boolean isMatured;

    FixedDepositAccount(long balance, boolean isMatured) {
        super(balance);
        this.isMatured = isMatured;
    }

    double interestRate() {
        return 7.0;
    }

    // Supplementary: withdrawal allowed only after maturity
    boolean canWithdraw(long amount) {
        return isMatured;
    }
}

public class Main {
    public static void main(String[] args) {
        Account[] accounts = {
            new SavingsAccount(5000, 1000),
            new CurrentAccount(2000, 5000),
            new FixedDepositAccount(10000, false)
        };

        for (Account acc : accounts) {
            System.out.println(acc.getClass().getSimpleName()
                + " -> interest rate = " + acc.interestRate());
            System.out.println("  Monthly interest = "
                + String.format("%.2f", acc.monthlyInterest()));
            System.out.println("  Can withdraw 4500? " + acc.canWithdraw(4500));

            if (acc instanceof FixedDepositAccount fd) {
                System.out.println("  [FD special] Matured? " + fd.isMatured);
            }
        }
    }
}
