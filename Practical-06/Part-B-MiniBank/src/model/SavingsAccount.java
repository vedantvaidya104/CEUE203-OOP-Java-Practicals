package model;

public class SavingsAccount extends Account implements Premium {
    private long minBalance;

    public SavingsAccount(long balance, long minBalance) {
        super(balance);
        this.minBalance = minBalance;
    }

    @Override
    public double interestRate() { return 4.0; }

    @Override
    public boolean canWithdraw(long amount) {
        return (balance - amount) >= minBalance;
    }
}
