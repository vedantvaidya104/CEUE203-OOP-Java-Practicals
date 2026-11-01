import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import static java.lang.Math.max;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Id {}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface Positive {
    String message() default "balance must be greater than 0";
}

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface MaxLength {
    int value();
}

class BankException extends Exception {
    public BankException(String message) {
        super(message);
    }
}

class InsufficientFundsException extends BankException {
    private final long shortfall;

    public InsufficientFundsException(String message, long shortfall) {
        super(message);
        this.shortfall = shortfall;
    }

    public long getShortfall() {
        return shortfall;
    }
}

class AccountNotFoundException extends BankException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}

class InvalidAmountException extends BankException {
    public InvalidAmountException(String message) {
        super(message);
    }
}

class BankAuditSession implements AutoCloseable {
    private final String sessionId;

    public BankAuditSession(String sessionId) {
        this.sessionId = sessionId;
        System.out.println("[AUDIT] Session opened : " + sessionId);
    }

    @Override
    public void close() {
        System.out.println("[AUDIT] Session closed : " + sessionId);
    }
}

class AccountWorker implements Runnable {
    private final Account target;
    private final int repeat;
    private final long unit;

    public AccountWorker(Account target, int repeat, long unit) {
        this.target = target;
        this.repeat = repeat;
        this.unit = unit;
    }

    @Override
    public void run() {
        for (int i = 0; i < repeat; i++) {
            try {
                target.deposit(unit);
            } catch (InvalidAmountException ignored) {
                // a non-positive unit is simply skipped
            }
        }
    }
}

class TransactionProcessor {
    private final ExecutorService pool;
    private final ConcurrentHashMap<String, Account> sharedAccounts = new ConcurrentHashMap<>();

    public TransactionProcessor(int poolSize) {
        this.pool = Executors.newFixedThreadPool(poolSize);
    }

    public void registerAccount(Account acc) {
        sharedAccounts.put(acc.getAccountNumber(), acc);
    }

    public Account getAccount(String accNo) {
        return sharedAccounts.get(accNo);
    }

    public void submit(Runnable task) {
        pool.execute(task);
    }

    public void stop() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

class TransactionBuffer {
    private final Queue<String> pending = new LinkedList<>();
    private final int capacity;

    public TransactionBuffer(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void produce(String tx) throws InterruptedException {
        while (pending.size() >= capacity) {
            wait();
        }
        pending.add(tx);
        System.out.println(" [buffer] produced -> " + tx);
        notifyAll();
    }

    public synchronized String consume() throws InterruptedException {
        while (pending.isEmpty()) {
            wait();
        }
        String tx = pending.poll();
        System.out.println(" [buffer] consumed <- " + tx);
        notifyAll();
        return tx;
    }
}

class StatePersister {
    public static void save(Account[] accounts, Path file) {
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(file.toFile()))) {
                out.writeObject(accounts);
            }
        } catch (Exception e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    public static Account[] load(Path file) {
        if (!Files.exists(file)) {
            return new Account[0];
        }
        try (ObjectInputStream in =
                 new ObjectInputStream(new FileInputStream(file.toFile()))) {
            return (Account[]) in.readObject();
        } catch (Exception e) {
            System.out.println("Error loading state: " + e.getMessage());
            return new Account[0];
        }
    }
}

class TransactionLog {
    public static void append(Path file, String line) {
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            byte[] data = (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8);
            Files.write(file, data, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.out.println("Error appending to log: " + e.getMessage());
        }
    }
}

class ReportGenerator {
    public static void generateReport(Path logsDir, Path reportFile) {
        long totalDeposits = 0;
        long totalWithdrawals = 0;
        int entriesRead = 0;

        try {
            if (reportFile.getParent() != null) {
                Files.createDirectories(reportFile.getParent());
            }

            if (Files.exists(logsDir)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(logsDir)) {
                    for (Path entry : stream) {
                        if (Files.isDirectory(entry)) {
                            continue;
                        }
                        try (BufferedReader reader = Files.newBufferedReader(entry)) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String[] parts = line.trim().split("\\s+");
                                if (parts.length < 3) {
                                    continue;
                                }
                                try {
                                    long amt = Long.parseLong(parts[2]);
                                    if (parts[0].equalsIgnoreCase("DEPOSIT")) {
                                         totalDeposits += amt;
                                         entriesRead++;
                                    } else if (parts[0].equalsIgnoreCase("WITHDRAW")) {
                                         totalWithdrawals += amt;
                                         entriesRead++;
                                    }
                                } catch (NumberFormatException ignored) {
                                       // skip malformed log line
                                   }
                               }
                           }
                       }
                   }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("====================================\n");
            sb.append("         END-OF-DAY REPORT          \n");
            sb.append("====================================\n");
            sb.append("Entries Processed : ").append(entriesRead).append("\n");
            sb.append("Total Deposits    : Rs. ").append(totalDeposits).append("\n");
            sb.append("Total Withdrawals : Rs. ").append(totalWithdrawals).append("\n");
            sb.append("Net Cashflow      : Rs. ")
              .append(totalDeposits - totalWithdrawals).append("\n");
            sb.append("====================================\n");

            Files.write(reportFile, sb.toString().getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.print(sb);

        } catch (Exception e) {
            System.out.println("Error generating report: " + e.getMessage());
        }
    }
}

class AccountStore {
    private final Map<String, Account> accounts = new HashMap<>();
    private final List<String> transactionHistory = new ArrayList<>();

    public void add(Account account) {
        if (account != null) {
            accounts.put(account.getAccountNumber(), account);
            transactionHistory.add("Added account: " + account.getAccountNumber());
        }
    }

    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public List<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    public void logTransaction(String log) {
        transactionHistory.add(log);
    }

    public List<String> getHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public List<Account> sortedByBalance() {
        List<Account> list = new ArrayList<>(accounts.values());
        list.sort((a1, a2) -> Long.compare(a2.getBalance(), a1.getBalance()));
        return list;
    }
}

class Repository<T> {
    private final HashMap<String, T> storage = new HashMap<>();

    private String extractId(T entity) {
        Class<?> clazz = entity.getClass();
        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                     field.setAccessible(true);
                     try {
                         Object val = field.get(entity);
                         return val != null ? val.toString() : null;
                     } catch (IllegalAccessException e) {
                         return null;
                     }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return null;
    }

    public T save(T entity) {
        if (entity == null) return null;
        String id = extractId(entity);
        if (id == null) return null;
        storage.put(id, entity);
        return entity;
    }

    @SafeVarargs
    public final void saveAll(T... items) {
        for (T item : items) {
            save(item);
        }
    }

    public T findById(String id) {
        return storage.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public boolean delete(String id) {
        return storage.remove(id) != null;
    }

    public int count() {
        return storage.size();
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) return 0.0;
        double sum = 0.0;
        for (N val : values) {
            sum += val.doubleValue();
        }
        return sum / values.length;
    }
}

record BankInfo(String name, String branch) {}

enum MenuOption {
    OPEN_ACCOUNT,
    DEPOSIT,
    WITHDRAW,
    TRANSFER,
    STATEMENT,
    LIST_ACCOUNTS,
    BATCH_PROCESS,
    RUN_REPORT,
    EXIT
}

enum TransactionType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER
}

record Command(TransactionType type, String accountNumber, long amount) {}

interface Transactable {
    void deposit(long amount) throws InvalidAmountException;
    boolean withdraw(long amount) throws InsufficientFundsException, InvalidAmountException;
}

interface InterestBearing {
    double interestRate();

    default double yearlyInterest(long balance) {
        return (balance * interestRate()) / 100.0;
    }
}

interface Premium {}

@FunctionalInterface
interface WithdrawRule {
    boolean allow(Account account, long amount);
}

class AnnotationValidator {
    public static String[] validate(Object obj) {
        if (obj == null) return new String[0];
        List<String> errors = new ArrayList<>();
        Class<?> clazz = obj.getClass();

        while (clazz != null && clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object val = field.get(obj);

                       if (field.isAnnotationPresent(Positive.class)
                               && val instanceof Number num
                               && num.longValue() <= 0) {
                           errors.add(field.getAnnotation(Positive.class).message());
                       }

                       if (field.isAnnotationPresent(MaxLength.class)
                               && val instanceof String str) {
                           int limit = field.getAnnotation(MaxLength.class).value();
                           if (str.length() > limit) {
                               errors.add(field.getName() + " exceeds " + limit + " characters");
                           }
                       }
                   } catch (IllegalAccessException ignored) {
                       // field not readable, nothing to validate
                   }
            }
            clazz = clazz.getSuperclass();
        }
        return errors.toArray(new String[0]);
    }
}

class Validator {
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[6-9][0-9]{9}$");
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PAN_PATTERN = Pattern.compile("^[A-Z]{5}[0-9]{4}[A-Z]$");
    private static final Pattern IFSC_PATTERN = Pattern.compile("^[A-Z]{4}0[A-Z0-9]{6}$");

    public static boolean isValidMobile(String mobile) {
        return mobile != null && MOBILE_PATTERN.matcher(mobile).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPan(String pan) {
        return pan != null && PAN_PATTERN.matcher(pan).matches();
    }

    public static boolean isValidIfsc(String ifsc) {
        return ifsc != null && IFSC_PATTERN.matcher(ifsc).matches();
    }
}

class CommandParser {
    public static Command parse(String line) {
        if (line == null) return null;
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 3) {
            return null;
        }

        try {
            TransactionType type = TransactionType.valueOf(parts[0].toUpperCase());
            long amount = Long.parseLong(parts[2]);
            return new Command(type, parts[1], amount);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

class StatementFormatter {
    public static String buildStatement(Account account) {
        StringBuilder sb = new StringBuilder();
        sb.append("====================================\n");
        sb.append("         ACCOUNT STATEMENT          \n");
        sb.append("====================================\n");
        sb.append("Account Number : ").append(account.getAccountNumber()).append("\n");
        sb.append("Account Holder : ").append(account.getOwnerName()).append("\n");
        sb.append("Account Type    : ").append(account.getClass().getSimpleName()).append("\n");
        sb.append("Current Balance : Rs. ").append(account.getBalance()).append("\n");
        sb.append("Withdrawable    : Rs. ").append(max(0, account.availableFunds())).append("\n");
        sb.append("Interest Rate   : ").append(account.interestRate()).append("%\n");
        sb.append("Yearly Interest : Rs. ")
          .append(String.format("%.2f", account.yearlyInterest(account.getBalance())))
          .append("\n");
        sb.append("Account Status : ").append(account.isActive() ? "Active" : "Inactive")
          .append("\n");
        sb.append("====================================");
        return sb.toString();
    }
}

class Customer implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;
    private static long customerCounter = 100;

    @Id
    private final String customerId;
    @MaxLength(50)
    private String name;
    private String email;
    private String mobile;
    private Address address;

    public static class Address implements Serializable {
        private static final long serialVersionUID = 1L;

        private final String line;
        private final String city;
        private final String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine() {
            return line;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }

        @Override
        public String toString() {
            return line + ", " + city + " - " + pincode;
        }
    }

    private static synchronized String nextCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }

    public Customer(String name, String email, String mobile, Address address) {
        this.customerId = nextCustomerId();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
    }

    public Customer(String name, String email, String mobile) {
        this(name, email, mobile, null);
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

abstract class Account
        implements Transactable, InterestBearing, Serializable, Comparable<Account> {
    private static final long serialVersionUID = 1L;
    private static final String PREFIX = "MB";
    private static long accountCounter = 0;

    @Id
    private final String accountNumber;
    @MaxLength(50)
    private String ownerName;
    @Positive
    protected long balance;
    private boolean active;

    public static void setCounter(long count) {
        accountCounter = count;
    }

    /** Re-seeds the counter from the highest number found in restored accounts. */
    public static void restoreCounter(Account[] existing) {
        long highest = 0;
        for (Account a : existing) {
            try {
                highest = max(highest, Long.parseLong(a.accountNumber.substring(PREFIX.length())));
            } catch (NumberFormatException ignored) {
                // custom account numbers are ignored
            }
        }
        accountCounter = highest;
    }

    private static synchronized String generateAccountNumber() {
        accountCounter++;
        return String.format("%s%05d", PREFIX, accountCounter);
    }

    public Account(String ownerName, long balance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    public Account(String accountNumber, String ownerName, long balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    @Override
    public int compareTo(Account other) {
        return this.accountNumber.compareTo(other.accountNumber);
    }

    @Override
    public abstract double interestRate();

    /** Maximum amount that may be withdrawn right now under this account's rules. */
    public abstract long availableFunds();

    public boolean canWithdraw(long amount) {
        return amount <= availableFunds();
    }

    @Override
    public synchronized void deposit(long amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be greater than zero.");
        }
        balance += amount;
    }

    @Override
    public synchronized boolean withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be greater than zero.");
        }
        if (!canWithdraw(amount)) {
            long shortfall = amount - availableFunds();
            throw new InsufficientFundsException(
                     "Withdrawal refused: insufficient withdrawable balance.", max(0, shortfall));
        }
        balance -= amount;
        return true;
    }

    public void transfer(Account to, long amount) throws BankException {
        if (to == null) {
            throw new AccountNotFoundException("Destination account not found.");
        }
        if (to == this) {
            throw new BankException("Source and destination accounts must be different.");
        }

        // Always lock in account-number order so two opposite transfers cannot deadlock.
        boolean thisFirst = this.accountNumber.compareTo(to.accountNumber) < 0;
        Account firstLock = thisFirst ? this : to;
        Account secondLock = thisFirst ? to : this;

        synchronized (firstLock) {
            synchronized (secondLock) {
                this.withdraw(amount);
                to.deposit(amount);
            }
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public synchronized long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return accountNumber + " | " + ownerName + " | " + getClass().getSimpleName()
                + " | Rs. " + balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNumber, account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}

class SavingsAccount extends Account {
    private static final long serialVersionUID = 1L;
    public static final long DEFAULT_MIN_BALANCE = 1500;
    private final long minBalance;

    public SavingsAccount(String ownerName, long balance, long minBalance) {
        super(ownerName, balance);
        this.minBalance = minBalance;
    }

    public SavingsAccount(String ownerName, long balance) {
        this(ownerName, balance, DEFAULT_MIN_BALANCE);
    }

    @Override
    public double interestRate() {
        return 4.5;
    }

    @Override
    public synchronized long availableFunds() {
        return balance - minBalance;
    }

    public long getMinBalance() {
        return minBalance;
    }
}

class CurrentAccount extends Account implements Premium {
    private static final long serialVersionUID = 1L;
    public static final long DEFAULT_OVERDRAFT = 10000;
    private final long overdraftLimit;

    public CurrentAccount(String ownerName, long balance, long overdraftLimit) {
        super(ownerName, balance);
        this.overdraftLimit = overdraftLimit;
    }

    public CurrentAccount(String ownerName, long balance) {
        this(ownerName, balance, DEFAULT_OVERDRAFT);
    }

    @Override
    public double interestRate() {
        return 0.0;
    }

    @Override
    public synchronized long availableFunds() {
        return balance + overdraftLimit;
    }

    public long getOverdraftLimit() {
        return overdraftLimit;
    }
}

class FixedDepositAccount extends Account {
    private static final long serialVersionUID = 1L;

    public FixedDepositAccount(String ownerName, long balance) {
        super(ownerName, balance);
    }

    @Override
    public double interestRate() {
        return 7.5;
    }

    @Override
    public long availableFunds() {
        return 0;
    }
}

class BankService {
    private static final Path LOG_PATH = Paths.get("logs", "transactions.log");

    private final Repository<Account> repository;
    private boolean loggingEnabled = true;

    public BankService(Repository<Account> repository) {
        this.repository = repository;
    }

    public void setLoggingEnabled(boolean enabled) {
        this.loggingEnabled = enabled;
    }

    private void record(String entry) {
        if (loggingEnabled) {
            TransactionLog.append(LOG_PATH, entry);
        }
    }

    private Account require(String accNo, String label) throws AccountNotFoundException {
        Account acc = repository.findById(accNo);
        if (acc == null) {
            throw new AccountNotFoundException(label + " " + accNo + " not found.");
        }
        return acc;
    }

    public Account openAccount(int type, String name, long openingBalance)
            throws BankException {
        if (name == null || name.isBlank()) {
            throw new BankException("Customer name cannot be empty.");
        }
        if (openingBalance <= 0) {
            throw new InvalidAmountException("Opening balance must be positive.");
        }

        Account acc = switch (type) {
            case 1 -> new SavingsAccount(name, openingBalance);
            case 2 -> new CurrentAccount(name, openingBalance);
            case 3 -> new FixedDepositAccount(name, openingBalance);
            default -> throw new BankException("Invalid account type selected.");
        };

        String[] problems = AnnotationValidator.validate(acc);
        if (problems.length > 0) {
            throw new BankException("Validation failed: " + String.join("; ", problems));
        }

        repository.save(acc);
        record("OPEN " + acc.getAccountNumber() + " " + openingBalance);
        return acc;
    }

    public void deposit(String accNo, long amount) throws BankException {
        require(accNo, "Account").deposit(amount);
        record("DEPOSIT " + accNo + " " + amount);
    }

    public void withdraw(String accNo, long amount) throws BankException {
        require(accNo, "Account").withdraw(amount);
        record("WITHDRAW " + accNo + " " + amount);
    }

    public void transfer(String fromAccNo, String toAccNo, long amount)
            throws BankException {
        Account from = require(fromAccNo, "Source account");
        Account to = require(toAccNo, "Destination account");

        from.transfer(to, amount);
        record("WITHDRAW " + fromAccNo + " " + amount);
        record("DEPOSIT " + toAccNo + " " + amount);
    }

    public String statement(String accNo) throws BankException {
        return StatementFormatter.buildStatement(require(accNo, "Account"));
    }

    public List<Account> listAccounts() {
        List<Account> list = repository.findAll();
        Collections.sort(list);
        return list;
    }

    public long totalHoldings() {
        long total = 0;
        for (Account a : repository.findAll()) {
            total += a.getBalance();
        }
        return total;
    }

    public void runBatch(TransactionProcessor processor, List<Command> commands) {
        for (Account a : repository.findAll()) {
            processor.registerAccount(a);
        }

        for (Command cmd : commands) {
            processor.submit(() -> {
                try {
                    switch (cmd.type()) {
                        case DEPOSIT -> deposit(cmd.accountNumber(), cmd.amount());
                        case WITHDRAW -> withdraw(cmd.accountNumber(), cmd.amount());
                        default -> System.out.println("Batch skipped unsupported: " + cmd);
                    }
                } catch (BankException e) {
                    System.out.println("Batch item rejected: " + e.getMessage());
                }
            });
        }
    }
}

public class MiniBank {
    private static final Path DATA_FILE = Paths.get("data", "accounts.dat");
    private static final Path LOGS_DIR = Paths.get("logs");
    private static final Path REPORT_FILE = Paths.get("reports", "daily_summary.txt");

    public static void printMenu() {
        System.out.println("\n========== MINIBANK CONSOLE ==========");
        System.out.println("1. Open Account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer");
        System.out.println("5. Account Statement");
        System.out.println("6. List All Accounts");
        System.out.println("7. Run Concurrent Batch Engine");
        System.out.println("8. Generate End-of-Day Report");
        System.out.println("9. Save and Exit");
        System.out.print("Enter choice (1-9): ");
    }

    private static String readText(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static long readNumber(Scanner sc, String prompt) {
        System.out.print(prompt);
        long value = sc.nextLong();
        sc.nextLine();
        return value;
    }

    private static void check(boolean condition, String label) {
        System.out.println((condition ? "[PASS] " : "[FAIL] ") + label);
    }

    public static void runSelfTests(BankService service, Repository<Account> repo) {
        System.out.println("\n--- Running Built-in Self Tests ---");
        service.setLoggingEnabled(false);
        try {
            Account savings = service.openAccount(1, "TestUser", 2000);
            service.deposit(savings.getAccountNumber(), 500);
            check(savings.getBalance() == 2500, "Deposit updates the balance.");

             boolean refused = false;
             try {
                 service.withdraw(savings.getAccountNumber(), 10000);
             } catch (InsufficientFundsException e) {
                 refused = true;
             }
             check(refused, "Over-withdrawal throws InsufficientFundsException.");

             Account fixed = service.openAccount(3, "FdUser", 5000);
             boolean fdBlocked = false;
             try {
                 service.withdraw(fixed.getAccountNumber(), 100);
             } catch (InsufficientFundsException e) {
                 fdBlocked = true;
             }
             check(fdBlocked, "Fixed deposit rejects withdrawals.");

             Account shared = service.openAccount(1, "ConcUser", 1000);
             service.transfer(savings.getAccountNumber(), shared.getAccountNumber(), 500);
             check(savings.getBalance() == 2000 && shared.getBalance() == 1500,
                     "Transfer moves funds between accounts.");

             Thread[] workers = new Thread[5];
             for (int i = 0; i < workers.length; i++) {
                 workers[i] = new Thread(new AccountWorker(shared, 200, 10));
                 workers[i].start();
             }
             for (Thread t : workers) {
                 t.join();
             }
             check(shared.getBalance() == 11500, "Concurrent deposits keep the exact balance.");

             repo.delete(savings.getAccountNumber());
             repo.delete(fixed.getAccountNumber());
            repo.delete(shared.getAccountNumber());
            System.out.println("[INFO] Self tests finished.\n");
        } catch (Exception e) {
            System.out.println("[FAIL] Self test aborted: " + e.getMessage());
        } finally {
            service.setLoggingEnabled(true);
        }
    }

    /** Producer-consumer demo: raw command lines flow through a bounded buffer. */
    private static List<Command> collectBatchCommands(String target)
            throws InterruptedException {
        final String[] raw = {
            "DEPOSIT " + target + " 250",
            "DEPOSIT " + target + " 350",
            "WITHDRAW " + target + " 100"
        };
        final TransactionBuffer buffer = new TransactionBuffer(2);
        final List<Command> parsed = new ArrayList<>();

        Thread producer = new Thread(() -> {
            try {
                for (String line : raw) {
                    buffer.produce(line);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < raw.length; i++) {
                    Command c = CommandParser.parse(buffer.consume());
                    if (c != null) {
                        parsed.add(c);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        return parsed;
    }

    public static void main(String[] args) {
        BankInfo info = new BankInfo("Charusat National Bank", "Changa Branch");
        System.out.println("MiniBank Console Banking - " + info.name()
                + " (" + info.branch() + ")");

        Repository<Account> repository = new Repository<>();

        Account[] loaded = StatePersister.load(DATA_FILE);
        if (loaded.length > 0) {
            repository.saveAll(loaded);
            Account.restoreCounter(loaded);
            System.out.println("Restored " + loaded.length + " account(s) from disk.");
        } else {
            SavingsAccount sa = new SavingsAccount("Vedant", 6000, 1500);
            CurrentAccount ca = new CurrentAccount("Aarav", 9000, 4000);
            repository.saveAll(sa, ca);
            System.out.println("No saved data found - created two starter accounts.");
        }

        BankService service = new BankService(repository);
        runSelfTests(service, repository);

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            if (!sc.hasNextInt()) {
                if (!sc.hasNext()) {
                    break;
                }
                System.out.println("Invalid numeric input. Please try again.");
                   sc.next();
                   continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            MenuOption opt = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.STATEMENT;
                case 6 -> MenuOption.LIST_ACCOUNTS;
                case 7 -> MenuOption.BATCH_PROCESS;
                case 8 -> MenuOption.RUN_REPORT;
                case 9 -> MenuOption.EXIT;
                default -> null;
            };

            if (opt == null) {
                System.out.println("Invalid menu choice. Select between 1 and 9.");
                continue;
            }

            try {
                switch (opt) {
                    case OPEN_ACCOUNT -> {
                        int type = (int) readNumber(sc,
                                 "\nAccount type (1 = Savings, 2 = Current, 3 = Fixed Deposit): ");
                        String name = readText(sc, "Enter customer name: ");
                        long bal = readNumber(sc, "Enter opening balance: ");
                        Account created = service.openAccount(type, name, bal);
                        System.out.println("Account opened: " + created);
                    }
                    case DEPOSIT -> {
                        String accNo = readText(sc, "Enter account number: ").toUpperCase();
                        long amt = readNumber(sc, "Enter deposit amount: ");
                        service.deposit(accNo, amt);
                        System.out.println("Deposit of Rs. " + amt + " completed.");
                    }
                    case WITHDRAW -> {
                        String accNo = readText(sc, "Enter account number: ").toUpperCase();
                        long amt = readNumber(sc, "Enter withdrawal amount: ");
                        service.withdraw(accNo, amt);
                        System.out.println("Withdrawal of Rs. " + amt + " completed.");
                    }
                    case TRANSFER -> {
                        String from = readText(sc, "Enter source account: ").toUpperCase();
                        String to = readText(sc, "Enter destination account: ").toUpperCase();
                        long amt = readNumber(sc, "Enter transfer amount: ");
                        service.transfer(from, to, amt);
                        System.out.println("Transfer of Rs. " + amt + " successful.");
                    }
                    case STATEMENT -> {
                        String accNo = readText(sc, "Enter account number: ").toUpperCase();
                        System.out.println(service.statement(accNo));
                    }
                    case LIST_ACCOUNTS -> {
                        System.out.println("\n--- Registered Accounts (sorted by number) ---");
                        List<Account> accounts = service.listAccounts();
                        if (accounts.isEmpty()) {
                            System.out.println("No accounts found.");
                        } else {
                            accounts.forEach(System.out::println);
                            System.out.println("Total holdings: Rs. " + service.totalHoldings());
                        }
                    }
                    case BATCH_PROCESS -> {
                        List<Account> current = service.listAccounts();
                        if (current.isEmpty()) {
                            System.out.println("Open an account before running a batch.");
                            break;
                        }
                        String sessionId = "BATCH-" + System.currentTimeMillis();
                        try (BankAuditSession audit = new BankAuditSession(sessionId)) {
                            String target = current.get(0).getAccountNumber();
                            System.out.println("Feeding commands through the buffer...");
                            List<Command> batch = collectBatchCommands(target);

                                System.out.println("Executing batch on a 4-thread pool...");
                              TransactionProcessor tp = new TransactionProcessor(4);
                              service.runBatch(tp, batch);
                              tp.stop();
                              System.out.println("Batch finished: " + batch.size()
                                      + " command(s) executed on " + target + ".");
                          }
                      }
                      case RUN_REPORT -> {
                          System.out.println("\nBuilding end-of-day report from the log files:");
                          ReportGenerator.generateReport(LOGS_DIR, REPORT_FILE);
                      }
                      case EXIT -> {
                          List<Account> all = repository.findAll();
                          StatePersister.save(all.toArray(new Account[0]), DATA_FILE);
                          System.out.println("Saved " + all.size() + " account(s) to " + DATA_FILE);
                          System.out.println("Thank you for using MiniBank. Goodbye!");
                          running = false;
                      }
                }
            } catch (InsufficientFundsException e) {
                System.out.println("Transaction Error: " + e.getMessage()
                        + " (Shortfall: Rs. " + e.getShortfall() + ")");
            } catch (BankException e) {
                System.out.println("Banking Error: " + e.getMessage());
            } catch (InputMismatchException e) {
                System.out.println("Please enter a whole number for amounts and types.");
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Unexpected system error: " + e.getMessage());
            }
        }
        sc.close();
    }
}
