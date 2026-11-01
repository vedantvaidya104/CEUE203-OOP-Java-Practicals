# Practical 8: Exception Handling

**CEUE203 - Object Oriented Programming** | Vedant Vaidya (25IT104) | CSPIT

## Tasks

- **Task-1**: 1.Guarded calculator: read two numbers and an operator; throw a custom DivideByZeroException and catch invalid-number input separately, printing a clear message each time; loop until a valid calculation succeeds and use finally to log each attempt.
- **Task-2**: 2.Stock issue: a Warehouse where issue(item, qty) throws a custom checked OutOfStockException carrying the shortfall, and InvalidQuantityException for qty ≤ 0. Process a list of requests, catching and reporting each failure without stopping the run.
- **Task-3**: 3.AutoCloseable Resource: (For Advanced Learners – home) Create an AutoCloseable resource and use try-with-resources to prove it always closes — even when an exception is thrown inside the block — and that the original error is the one reported. Decide what to model as the resource.

## Build and run

Requires JDK 17 or newer. Run these from the repository root.

### Task-1

```bash
cd Practical-08/Task-1
javac -d out $(find . -name '*.java')
java -cp out Main
```

### Task-2

```bash
cd Practical-08/Task-2
javac -d out $(find . -name '*.java')
java -cp out Main
```

### Task-3

```bash
cd Practical-08/Task-3
javac -d out $(find . -name '*.java')
java -cp out Main
```
