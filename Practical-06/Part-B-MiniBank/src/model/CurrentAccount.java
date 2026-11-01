package model;

public class CurrentAccount extends Account {
    private long overdraftLimit;

    public CurrentAccount(long balance, long overdraftLimit) {
        super(balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public double interestRate() { return 0.0; }

    @Override
    public boolean canWithdraw(long amount) {
        return (balance - amount) >= -overdraftLimit;
    }
}
