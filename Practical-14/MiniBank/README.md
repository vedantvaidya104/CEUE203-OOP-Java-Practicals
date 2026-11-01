MiniBank - Practical 14 (Final Capstone)
========================================
A console banking application: accounts, deposits, withdrawals,
transfers, statements, a thread-pool batch processor, persistence
and an end-of-day report.

Requirements
  JDK 17 or newer, JUnit 5 console launcher (only to run the tests)

Build
  javac -d out/main $(find src/main -name "*.java")
  echo Main-Class: minibank.Main > manifest.txt
  jar cfm dist/MiniBank.jar manifest.txt -C out/main .

Run
  java -jar dist/MiniBank.jar
  (accounts.dat and logs/ are created in the current folder)

Menu
  1 Open account     2 Deposit        3 Withdraw
  4 Transfer         5 Statement      6 List accounts
  7 Batch deposits on thread pool     8 End-of-day report
  9 Search by owner  0 Save and exit

Test
  java -jar junit-platform-console-standalone.jar \
       -cp out/main:out/test --scan-classpath

Design
  Main (menu) -> BankService -> Repository<Account> (ConcurrentHashMap)
  Account methods are synchronized; transfers lock the smaller
  account number first, so two opposite transfers cannot deadlock.
  Data is saved to accounts.dat on exit and loaded at start-up.
