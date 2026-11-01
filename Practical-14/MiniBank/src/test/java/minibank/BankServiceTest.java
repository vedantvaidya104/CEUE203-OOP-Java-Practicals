package minibank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import minibank.model.Account;
import minibank.model.InsufficientFundsException;
import minibank.model.InvalidInputException;
import minibank.repo.AccountRepository;
import minibank.service.BankService;
import minibank.service.TransactionProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankServiceTest {
    private BankService bank;
    private Account account;

    @BeforeEach
    void setUp() {
        bank = new BankService(new AccountRepository(), null);
        account = bank.openAccount("Asha", 500.0);
    }

    @Test
    void depositIncreasesBalance() {
        bank.deposit(account.getAccountNumber(), 250.0);
        assertEquals(750.0, account.getBalance(), 0.001);
    }

    @Test
    void withdrawOverLimitThrowsException() {
        assertThrows(InsufficientFundsException.class,
            () -> bank.withdraw(account.getAccountNumber(), 9999.0));
        assertEquals(500.0, account.getBalance(), 0.001);
    }

    @Test
    void invalidAmountThrowsException() {
        assertThrows(InvalidInputException.class,
            () -> bank.deposit(account.getAccountNumber(), -5.0));
    }

    @Test
    void transferMovesMoneyBetweenAccounts() {
        Account other = bank.openAccount("Ravi", 100.0);
        bank.transfer(account.getAccountNumber(), other.getAccountNumber(), 200.0);
        assertEquals(300.0, account.getBalance(), 0.001);
        assertEquals(300.0, other.getBalance(), 0.001);
    }

    @Test
    void concurrentDepositsKeepBalanceCorrect() {
        Account fresh = bank.openAccount("Shared", 0.0);
        TransactionProcessor processor = new TransactionProcessor();
        for (int t = 0; t < 10; t++) {
            processor.submit(() -> {
                for (int i = 0; i < 1000; i++) {
                    bank.deposit(fresh.getAccountNumber(), 1.0);
                }
            });
        }
        processor.stop();
        assertEquals(10000.0, fresh.getBalance(), 0.001);
    }

    @Test
    void saveAndLoadKeepsDataUnchanged() throws Exception {
        Path file = Files.createTempFile("accounts", ".dat");
        bank.save(file);
        BankService reloaded = new BankService(new AccountRepository(), null);
        reloaded.load(file);
        Account copy = reloaded.listAccounts().get(0);
        assertEquals(account.getOwner(), copy.getOwner());
        assertEquals(account.getBalance(), copy.getBalance(), 0.001);
        Files.deleteIfExists(file);
    }
}
