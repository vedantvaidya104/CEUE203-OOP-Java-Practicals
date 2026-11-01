# CEUE203 - Object Oriented Programming (Java) Practicals

**Name:** Vedant Vaidya  
**Roll No.:** 25IT104  
**Class:** IT-2, CSPIT (Chandubhai S. Patel Institute of Technology, CHARUSAT)  
**Subject:** CEUE203 - Object Oriented Programming

All programs for Practicals 1 to 14 and the MiniBank project.

## Contents

| No. | Practical | Folder |
|----:|-----------|--------|
| 1 | Java Fundamentals and MiniBank Shell | [Practical-01](Practical-01) |
| 2 | Classes and Objects - Account and Customer | [Practical-02](Practical-02) |
| 3 | Object Class Methods, Nested Classes and Cloneable | [Practical-03](Practical-03) |
| 4 | String Handling, StringBuilder and RegEx | [Practical-04](Practical-04) |
| 5 | Inheritance and Polymorphism | [Practical-05](Practical-05) |
| 6 | Interfaces, Functional Interface, Packages and JAR | [Practical-06](Practical-06) |
| 7 | Custom Annotations | [Practical-07](Practical-07) |
| 8 | Exception Handling | [Practical-08](Practical-08) |
| 9 | Thread Synchronization and Race Conditions | [Practical-09](Practical-09) |
| 10 | Thread Pools, Producer-Consumer and Deadlock | [Practical-10](Practical-10) |
| 11 | File I/O, Serialization, NIO Report | [Practical-11](Practical-11) |
| 12 | Collection Framework | [Practical-12](Practical-12) |
| 13 | Generics | [Practical-13](Practical-13) |
| 14 | Capstone - MiniBank Assembly, JUnit Testing and Runnable JAR | [Practical-14](Practical-14) |
| - | MiniBank project (single file) | [MiniBank-Project](MiniBank-Project) |

## Requirements

- JDK 17 or newer (`javac -version` to check)
- Practical 14 tests use the JUnit 5 console launcher (see `Practical-14/README.md`)

## How to run any task

Each practical folder has its own README with exact commands. In general:

```bash
cd Practical-01/Task-1
javac -d out $(find . -name '*.java')
java -cp out VendingMachine
```

Practical 6 and 14 use packages, so the commands in their READMEs include the package name.
