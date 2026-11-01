# Practical 1: Java Fundamentals and MiniBank Shell

**CEUE203 - Object Oriented Programming** | Vedant Vaidya (25IT104) | CSPIT

## Tasks

- **Task-1**: 1.Vending machine (VendingMachine.java): define an enum Coin (ONE, TWO, FIVE, TEN); read coin names in a loop, use a switch expression to convert each Coin to its value, keep a running total against a snack price of 15, stop once the total reaches 15 or more, and print the change to return.
- **Task-2**: 2.Toll booth (TollBooth.java): define a record Vehicle(String number, String type); loop until the user types "done", use a switch expression on the type for the toll (bike→20, car→50, truck→150), keep counters for each type, then print the total toll and the most frequent vehicle type.
- **Task-3**: 3.Rock-Paper-Scissors-Lizard-Spock (RPSLS.java) (For Advanced Learners): define an enum Move; write winner(Move a, Move b) returning 1, -1 or 0 using a switch expression; play 5 rounds against a random computer Move, print each round result, keep the score and print the overall winner.
- **Part-B-MiniBank**: Part B — MiniBank: create the public class MiniBank with main, a record BankInfo(name, branch) printed as the header, an enum MenuOption for the actions, a numbered menu read with Scanner, and a switch expression that prints a placeholder for each option; loop until EXIT. Supplementary: invalid menu numbers are re-prompted and a Working Hours option is added.

## Build and run

Requires JDK 17 or newer. Run these from the repository root.

### Part-B-MiniBank

```bash
cd Practical-01/Part-B-MiniBank
javac -d out $(find . -name '*.java')
java -cp out MiniBank
```

### Task-1

```bash
cd Practical-01/Task-1
javac -d out $(find . -name '*.java')
java -cp out VendingMachine
```

### Task-2

```bash
cd Practical-01/Task-2
javac -d out $(find . -name '*.java')
java -cp out TollBooth
```

### Task-3

```bash
cd Practical-01/Task-3
javac -d out $(find . -name '*.java')
java -cp out RPSLS
```
