# Practical 11: File I/O, Serialization, NIO Report

**CEUE203 - Object Oriented Programming** | Vedant Vaidya (25IT104) | CSPIT

## Tasks

- **Task-1**: 1. Save/load: make a small class Serializable, save an array of its objects to a file with ObjectOutputStream, then read it back into fresh objects and confirm the values survived. Mark one field transient and show it is not saved.
- **Task-2**: 2. Log analyzer: create a few text files, then use Path/Files to read each line by line and count the total lines and how many contain a keyword across all files; also print each file’s size from its attributes.
- **Task-3**: 3. Walk a folder tree with NIO and produce a report of the files it contains (name, size, modified time), writing the summary to a file; decide what to include.

## Build and run

Requires JDK 17 or newer. Run these from the repository root.

### Task-1

```bash
cd Practical-11/Task-1
javac -d out $(find . -name '*.java')
java -cp out SaveLoad
```

### Task-2

```bash
cd Practical-11/Task-2
javac -d out $(find . -name '*.java')
java -cp out LogAnalyzer
```

### Task-3

```bash
cd Practical-11/Task-3
javac -d out $(find . -name '*.java')
java -cp out FolderReport
```
