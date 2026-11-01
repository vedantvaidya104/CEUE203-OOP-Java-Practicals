# MiniBank - Console Banking Application

**Submitted by:** Vedant Vaidya (25IT104) | **Language:** Java 17+

A menu-driven console banking application covering accounts, transactions, concurrency,
persistence, validation and reporting. The whole program is in a single file, `MiniBank.java`.

## Features

- **Accounts** - Savings (4.5%), Current (overdraft up to Rs. 10000) and Fixed Deposit (7.5%, no withdrawals), numbered `MB00001` style
- **Transactions** - deposit, withdraw and a deadlock-safe transfer using ordered locking
- **Concurrency** - synchronized methods, a fixed thread pool and a producer-consumer buffer
- **Persistence** - object serialization of accounts plus an append-only transaction log
- **Reporting** - end-of-day report built by scanning log files with Java NIO
- **Validation** - custom annotations read through reflection, plus regex validators (mobile, email, PAN, IFSC)
- **Error handling** - custom checked-exception hierarchy and try-with-resources audit sessions
- **Self tests** - built-in checks for deposit, overdraft, fixed-deposit lock, transfer and thread safety

## Menu

| No. | Operation |
|----:|-----------|
| 1 | Open Account |
| 2 | Deposit |
| 3 | Withdraw |
| 4 | Transfer |
| 5 | Account Statement |
| 6 | List All Accounts |
| 7 | Run Concurrent Batch Engine |
| 8 | Generate End-of-Day Report |
| 9 | Save and Exit |

## Build and run

```bash
cd MiniBank-Project
javac MiniBank.java
java MiniBank
```

Accounts are saved to `data/accounts.dat`, transactions are logged under `logs/` and the
end-of-day report is written to `reports/daily_summary.txt` (all created in the current folder).
