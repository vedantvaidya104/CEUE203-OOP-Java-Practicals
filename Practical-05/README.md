# Practical 5: Inheritance and Polymorphism

**CEUE203 - Object Oriented Programming** | Vedant Vaidya (25IT104) | CSPIT

## Tasks

- **Task-1**: 1.Shape areas: an abstract Shape with abstract area(); subclasses Circle, Rectangle, Triangle. Put a mix in a Shape[] and loop once to print each area and the running total, tracking the largest — one loop handling every shape through polymorphism.
- **Task-2**: 2.Payroll: an abstract Employee with abstract monthlySalary(); FullTime (fixed), PartTime (hours × rate) and Intern (stipend), each calling super for the shared name/id. Loop a mixed array to print each salary and the total; use instanceof to add a note only for interns.
- **Task-3**: 3.Media late fees (For Advanced Learners): model a few media types that each compute a late fee differently and total the fees for a returned batch. Book charges Rs. 2/day, DVD charges Rs. 5/day capped at Rs. 50, and Magazine gives 3 free days then Rs. 1/day.
- **Part-B-MiniBank**: Part B — MiniBank: make Account abstract with abstract interestRate() and canWithdraw(long amount); add SavingsAccount (4.0%, balance must stay ≥ minBalance), CurrentAccount (0.0%, balance may fall to −overdraftLimit) and FixedDepositAccount (7.0%, locked until matured). Each subclass calls super(...). In main, loop an Account[] calling interestRate() and use a pattern instanceof for one type. Supplementary: monthlyInterest() in Account and maturity-based canWithdraw() in FixedDepositAccount.

## Build and run

Requires JDK 17 or newer. Run these from the repository root.

### Part-B-MiniBank

```bash
cd Practical-05/Part-B-MiniBank
javac -d out $(find . -name '*.java')
java -cp out Main
```

### Task-1

```bash
cd Practical-05/Task-1
javac -d out $(find . -name '*.java')
java -cp out Main
```

### Task-2

```bash
cd Practical-05/Task-2
javac -d out $(find . -name '*.java')
java -cp out Main
```

### Task-3

```bash
cd Practical-05/Task-3
javac -d out $(find . -name '*.java')
java -cp out Main
```
