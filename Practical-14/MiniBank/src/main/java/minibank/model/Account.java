package minibank.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int accountNumber;
    private final String owner;
    private double balance;
    private final List<String> statement = new ArrayList<>();

    public Account(int accountNumber, String owner, double openingBalance) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = openingBalance;
        statement.add(String.format("OPEN      %10.2f  balance %10.2f",
                openingBalance, balance));
    }

    public int getAccountNumber() { return accountNumber; }

    public String getOwner() { return owner; }

    public synchronized double getBalance() { return balance; }

    public synchronized void deposit(double amount) {
        balance += amount;
        statement.add(String.format("DEPOSIT   %10.2f  balance %10.2f",
                amount, balance));
    }

    public synchronized void withdraw(double amount) {
        if (amount > balance) {
            throw new InsufficientFundsException(
                "Insufficient funds in account " + accountNumber
                + ": balance " + balance + ", requested " + amount);
        }
        balance -= amount;
        statement.add(String.format("WITHDRAW  %10.2f  balance %10.2f",
                amount, balance));
    }

    public synchronized List<String> getStatement() {
        return Collections.unmodifiableList(new ArrayList<>(statement));
    }

    @Override
    public String toString() {
        return String.format("Acc#%d | %-10s | %10.2f",
                accountNumber, owner, getBalance());
    }
}
