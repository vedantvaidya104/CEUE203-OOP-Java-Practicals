# Practical 6: Interfaces, Functional Interface, Packages and JAR

**CEUE203 - Object Oriented Programming** | Vedant Vaidya (25IT104) | CSPIT

## Tasks

- **Task-1**: 1.Remote control: an interface Switchable with on()/off() and a default toggle(); Fan and Light implement it. Loop over a Switchable[] toggling each. Then a functional interface (SwitchRule) decides, given a device and an hour, whether it may switch on, supplied once as an anonymous class and once as a lambda.
- **Task-2**: 2.Notification senders: a functional interface Notifier with send(String); email and SMS senders are lambdas held in an array and used to broadcast one message. A marker interface Urgent makes the message go out twice for urgent senders.
- **Task-3**: 3.Discount engine (For Advanced Learners): each discount rule is a lambda picked by the user at runtime and applied to a list of prices; the code is organised in the package discount and shipped as a runnable JAR.
- **Part-B-MiniBank**: Part B — MiniBank: Transactable and InterestBearing (with default yearlyInterest()) interfaces, a functional interface WithdrawRule used with an anonymous class and a lambda, a marker interface Premium, classes organised into the packages model, util and service with one static import, and a runnable minibank.jar whose manifest Main-Class is service.MiniBank. Supplementary: a second default method monthlyInterest() and static import of the Validator methods.

## Build and run

Requires JDK 17 or newer. Run these from the repository root.

### Part-B-MiniBank

```bash
cd Practical-06/Part-B-MiniBank/src
javac -d out $(find . -name '*.java')
java -cp out service.MiniBank
```

### Task-1

```bash
cd Practical-06/Task-1
javac -d out $(find . -name '*.java')
java -cp out RemoteControl
```

### Task-2

```bash
cd Practical-06/Task-2
javac -d out $(find . -name '*.java')
java -cp out Notify
```

### Task-3

```bash
cd Practical-06/Task-3
javac -d out $(find . -name '*.java')
java -cp out discount.DiscountApp
```
