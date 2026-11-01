# Practical 13: Generics

**CEUE203 - Object Oriented Programming** | Vedant Vaidya (25IT104) | CSPIT

## Tasks

- **Task-1**: 1. Generic containers: a generic Box<T> with get/set and a Pair<A,B>; use them with different types to show type safety without casts, and write a generic method that returns the first element of any array.
- **Task-2**: 2. Bounded stats: a generic method <N extends Number> double average(N[] values) that works for Integer[], Double[] and Long[]; note why it refuses non-Number types.
- **Task-3**: 3. Implement a small generic container of your choice (e.g. a stack) backed by an array, reuse it for two different types, and handle the empty case sensibly.

## Build and run

Requires JDK 17 or newer. Run these from the repository root.

### Task-1

```bash
cd Practical-13/Task-1
javac -d out $(find . -name '*.java')
java -cp out GenericContainers
```

### Task-2

```bash
cd Practical-13/Task-2
javac -d out $(find . -name '*.java')
java -cp out BoundedStats
```

### Task-3

```bash
cd Practical-13/Task-3
javac -d out $(find . -name '*.java')
java -cp out GenericStack
```
