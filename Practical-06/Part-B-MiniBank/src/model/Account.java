package model;

import util.Validator;

public abstract class Account implements Transactable, InterestBearing {
    protected long balance;

    public Account(long balance) {
        this.balance = balance;
    }

    public abstract boolean canWithdraw(long amount);

    @Override
    public void deposit(long amount) {
        if (Validator.isPositive(amount)) {
            balance += amount;
        }
    }

    @Override
    public boolean withdraw(long amount) {
        if (Validator.isPositive(amount) && canWithdraw(amount)) {
            balance -= amount;
            return true;
        }
        return false;
    }

    @Override
    public long getBalance() {
        return balance;
    }
}
