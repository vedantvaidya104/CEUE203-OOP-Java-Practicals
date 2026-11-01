package minibank.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import minibank.io.ReportGenerator;
import minibank.io.StatePersister;
import minibank.io.TransactionLog;
import minibank.model.Account;
import minibank.model.AccountNotFoundException;
import minibank.model.BankException;
import minibank.repo.Repository;

public class BankService {
    private final Repository<Account> repository;
    private final Path logsDir;               // null = logging disabled
    private final AtomicInteger nextNumber = new AtomicInteger(1001);

    public BankService(Repository<Account> repository, Path logsDir) {
        this.repository = repository;
        this.logsDir = logsDir;
    }

    public Account openAccount(String owner, double opening) {
        Validator.requireName(owner);
        if (opening < 0) {
            throw new minibank.model.InvalidInputException(
                "Opening balance cannot be negative");
        }
        Account account =
            new Account(nextNumber.getAndIncrement(), owner.trim(), opening);
        repository.save(account);
        log("OPEN account=" + account.getAccountNumber());
        return account;
    }

    public void deposit(int accountNumber, double amount) {
        Validator.requirePositive(amount);
        find(accountNumber).deposit(amount);
        log("DEPOSIT account=" + accountNumber + " amount=" + amount);
    }

    public void withdraw(int accountNumber, double amount) {
        Validator.requirePositive(amount);
        find(accountNumber).withdraw(amount);
        log("WITHDRAW account=" + accountNumber + " amount=" + amount);
    }

    public void transfer(int fromNumber, int toNumber, double amount) {
        Validator.requirePositive(amount);
        if (fromNumber == toNumber) {
            throw new minibank.model.InvalidInputException(
                "Cannot transfer to the same account");
        }
        Account from = find(fromNumber);
        Account to = find(toNumber);
        // Practical 10: always lock the smaller account number first
        Account first = fromNumber < toNumber ? from : to;
        Account second = (first == from) ? to : from;
        synchronized (first) {
            synchronized (second) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
        log("TRANSFER from=" + fromNumber + " to=" + toNumber
                + " amount=" + amount);
    }

    public List<String> statement(int accountNumber) {
        return find(accountNumber).getStatement();
    }

    public List<Account> listAccounts() {
        List<Account> all = new ArrayList<>(repository.findAll());
        all.sort(Comparator.comparingInt(Account::getAccountNumber));
        return all;
    }

    public List<Account> searchByOwner(String text) {
        List<Account> result = new ArrayList<>();
        for (Account a : listAccounts()) {
            if (a.getOwner().toLowerCase().contains(text.toLowerCase())) {
                result.add(a);
            }
        }
        return result;
    }

    // Practical 10 + 12: many deposits run on the pool over the safe store
    public void processBatch(int accountNumber, int tasks, double amount) {
        find(accountNumber);                       // fail fast if missing
        TransactionProcessor processor = new TransactionProcessor();
        for (int i = 0; i < tasks; i++) {
            processor.submit(() -> deposit(accountNumber, amount));
        }
        processor.stop();
    }

    // Practical 11: persistence and report
    public void save(Path file) throws IOException {
        StatePersister.save(listAccounts().toArray(new Account[0]), file);
    }

    public int load(Path file) throws IOException, ClassNotFoundException {
        Account[] accounts = StatePersister.load(file);
        for (Account a : accounts) {
            repository.save(a);
            if (a.getAccountNumber() >= nextNumber.get()) {
                nextNumber.set(a.getAccountNumber() + 1);
            }
        }
        return accounts.length;
    }

    public String report() throws IOException {
        if (logsDir == null) {
            throw new BankException("Logging is disabled");
        }
        return ReportGenerator.generate(logsDir);
    }

    private Account find(int accountNumber) {
        return repository.findById(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    private void log(String line) {
        if (logsDir == null) return;
        try {
            Files.createDirectories(logsDir);
            TransactionLog.append(logsDir.resolve("transactions.log"), line);
        } catch (IOException e) {
            throw new BankException("Could not write log: " + e.getMessage());
        }
    }
}
