# Practical 7: Custom Annotations

**CEUE203 - Object Oriented Programming** | Vedant Vaidya (25IT104) | CSPIT

## Tasks

- **Task-1**: 1. Form validator: define @NotBlank and @MaxLength(int) for fields and annotate a SignupForm. Write a checker that uses reflection to read each field’s annotations and returns the list of validation errors for a given object.
- **Task-2**: 2. Mini test runner: define a @Run annotation and mark some no-arg methods of a class with it. Use reflection to find and invoke only the @Run methods and report how many ran — the idea behind how JUnit finds @Test methods.
- **Task-3**: 3. Define a @Column(name) annotation and, by reflection, populate an object’s fields from a header row and a data row by matching column names; decide how to handle a missing column.

## Build and run

Requires JDK 17 or newer. Run these from the repository root.

### Task-1

```bash
cd Practical-07/Task-1
javac -d out $(find . -name '*.java')
java -cp out FormValidator
```

### Task-2

```bash
cd Practical-07/Task-2
javac -d out $(find . -name '*.java')
java -cp out MiniTestRunner
```

### Task-3

```bash
cd Practical-07/Task-3
javac -d out $(find . -name '*.java')
java -cp out ColumnMapper
```
