package model;

public class FixedDepositAccount extends Account {
    private boolean matured;

    public FixedDepositAccount(long balance, boolean matured) {
        super(balance);
        this.matured = matured;
    }

    @Override
    public double interestRate() { return 7.0; }

    @Override
    public boolean canWithdraw(long amount) {
        return matured;
    }
}
