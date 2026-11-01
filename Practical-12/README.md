# Practical 12: Collection Framework

**CEUE203 - Object Oriented Programming** | Vedant Vaidya (25IT104) | CSPIT

## Tasks

- **Task-1**: 1. Word frequency: split a paragraph into words, count each in a HashMap, and print the words sorted by frequency (highest first) using a Comparator.
- **Task-2**: 2. Unique visitors: from a list of login names with repeats, use a HashSet to count unique users and a LinkedHashSet to keep first-seen order; print the unique count and the first three distinct names.
- **Task-3**: 3. Build a leaderboard from player scores held in a Map and print the top-N sorted by score (ties broken by name); choose the data and sorting details.

## Build and run

Requires JDK 17 or newer. Run these from the repository root.

### Task-1

```bash
cd Practical-12/Task-1
javac -d out $(find . -name '*.java')
java -cp out WordFrequency
```

### Task-2

```bash
cd Practical-12/Task-2
javac -d out $(find . -name '*.java')
java -cp out UniqueVisitors
```

### Task-3

```bash
cd Practical-12/Task-3
javac -d out $(find . -name '*.java')
java -cp out Leaderboard
```
