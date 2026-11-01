# Practical 14: Capstone - MiniBank Assembly, JUnit Testing and Runnable JAR

**CEUE203 - Object Oriented Programming** | Vedant Vaidya (25IT104) | CSPIT

## Layout

- `Part-A/Task-1` - layered Book app (model, service with `ArrayList`, menu) with JUnit 5 tests
- `Part-A/Task-2` - the same app organised into packages (`library.*`), runnable JAR and a save/reload test
- `MiniBank/` - the final capstone project (Maven-style `src/main/java` and `src/test/java`). See `MiniBank/README.md`.

## Part A, Task 1

```bash
cd Practical-14/Part-A/Task-1
javac -d out $(find . -name '*.java' ! -name '*Test.java')
java -cp out BookApp
```

## Part A, Task 2 (runnable JAR)

```bash
cd Practical-14/Part-A/Task-2
javac -d out $(find library -name '*.java' ! -name '*Test.java')
jar cfm Library.jar manifest.txt -C out .
java -jar Library.jar
```

## MiniBank capstone

```bash
cd Practical-14/MiniBank
mkdir -p out/main dist
javac -d out/main $(find src/main -name '*.java')
jar cfm dist/MiniBank.jar manifest.txt -C out/main .
java -jar dist/MiniBank.jar
```

### Run the JUnit 5 tests

Download `junit-platform-console-standalone.jar` from Maven Central, put it in `MiniBank/`, then:

```bash
mkdir -p out/test
javac -cp junit-platform-console-standalone.jar -d out/main $(find src/main -name '*.java')
javac -cp junit-platform-console-standalone.jar:out/main -d out/test $(find src/test -name '*.java')
java -jar junit-platform-console-standalone.jar -cp out/main:out/test --scan-classpath
```
