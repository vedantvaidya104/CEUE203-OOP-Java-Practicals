# Practical 9: Thread Synchronization and Race Conditions

**CEUE203 - Object Oriented Programming** | Vedant Vaidya (25IT104) | CSPIT

## Tasks

- **Task-1**: 1. Counter race: several threads each increment a shared counter many times. Run it without synchronization to watch the total come out too low, then make the increment synchronized and confirm it is exactly right every run.
- **Task-2**: 2. Seat booking race: a shared seatsLeft of 5 with about 10 threads each trying to book one seat. Without synchronization show that more than 5 can succeed (oversell); with a synchronized book() exactly 5 do.
- **Task-3**: 3. Split a large array of numbers across several threads that add into one shared total, show the wrong result, then fix it two ways and compare; decide how to measure the difference.

## Build and run

Requires JDK 17 or newer. Run these from the repository root.

### Task-1

```bash
cd Practical-09/Task-1
javac -d out $(find . -name '*.java')
java -cp out CounterRace
```

### Task-2

```bash
cd Practical-09/Task-2
javac -d out $(find . -name '*.java')
java -cp out SeatBooking
```

### Task-3

```bash
cd Practical-09/Task-3
javac -d out $(find . -name '*.java')
java -cp out ArraySum
```
