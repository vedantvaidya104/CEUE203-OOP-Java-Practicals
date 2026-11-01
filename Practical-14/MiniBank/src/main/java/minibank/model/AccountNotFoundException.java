package minibank.model;

public class AccountNotFoundException extends BankException {
    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(int accountNumber) {
        super("Account not found: " + accountNumber);
    }
}
