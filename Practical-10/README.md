# Practical 10: Thread Pools, Producer-Consumer and Deadlock

**CEUE203 - Object Oriented Programming** | Vedant Vaidya (25IT104) | CSPIT

## Tasks

- **Task-1**: 1. Thread-pool runner: with a fixed pool of 3 (ExecutorService), submit about 10 Runnable tasks that print their id and sleep briefly; shut down and await completion, observing the few threads being reused.
- **Task-2**: 2. Producer–consumer: one thread adds numbers to a small shared buffer and another removes them, coordinated with wait()/notify(); produce and consume a fixed number of items in order with none lost.
- **Task-3**: 3. Reproduce a deadlock between two threads that lock two objects in opposite order, then remove it with a consistent lock order. Choose the scenario yourself.

## Build and run

Requires JDK 17 or newer. Run these from the repository root.

### Task-1

```bash
cd Practical-10/Task-1
javac -d out $(find . -name '*.java')
java -cp out ThreadPoolRunner
```

### Task-2

```bash
cd Practical-10/Task-2
javac -d out $(find . -name '*.java')
java -cp out ProducerConsumer
```

### Task-3

```bash
cd Practical-10/Task-3
javac -d out $(find . -name '*.java')
java -cp out DeadLockDemo
```
