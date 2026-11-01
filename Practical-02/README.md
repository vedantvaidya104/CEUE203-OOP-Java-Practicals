# Practical 2: Classes and Objects - Account and Customer

**CEUE203 - Object Oriented Programming** | Vedant Vaidya (25IT104) | CSPIT

## Tasks

- **Task-2.1**: 2.1)   Smart thermostat (Thermostat.java, main in this file) (a) Create class Thermostat with private String location, private int temperature, private static final int MIN=16 and MAX=30, and private static int activeCount=0. (b) Constructor(location, startTemp): store location; set temperature to startTemp if it is within MIN..MAX else 22; increment activeCount. (c) Constructor(location): chain with this(location, 22). (d) raise(): if temperature < MAX add 1, else print “Already at maximum (30)”. (e) lower(): if temperature > MIN subtract 1, else print “Already at minimum (16)”. (f) Add getTemperature() (no setter) and static getActiveCount(). (g) In main: create two thermostats, call raise() 10 times then lower() 20 times in loops (printing the temperature each time), then print getActiveCount(). Expected output: temperature rises to 30 then “Already at maximum”, falls to 16 then “Already at minimum”, count 2.
- **Task-2.2**: 2.2) Cinema show (CinemaShow.java, main in this file) (a) Create class CinemaShow with private String title, private int seatsAvailable, private final int capacity, and private static int totalBooked=0. (b) Constructor(title, capacity): set both and seatsAvailable = capacity. Constructor(title): chain with this(title, 100). (c) book(int n): if n ≤ seatsAvailable, reduce seatsAvailable by n, add n to totalBooked, return true; else return false unchanged. (d) cancel(int n): increase seatsAvailable by n but never above capacity. (e) Add getSeatsAvailable() and static getTotalBooked(). (f) In main: run a sequence of book and cancel calls, printing the result and seats after each, then print getTotalBooked(). Expected output: booking more than available prints false and leaves seats unchanged; totalBooked equals the successful bookings.
- **Task-2.3**: 2.3) (For Advanced Learners – home) Parking lot (ParkingLot.java) (a) Create class ParkingLot with private int twoWheelers and fourWheelers, private final int twoCap and fourCap, and private static long revenue=0. (b) park(String type): for “two”, if twoWheelers < twoCap increment and add 20 to revenue; for “four”, if fourWheelers < fourCap increment and add 40; otherwise print “Full” and reject. (c) leave(String type): decrement the matching count but never below 0. (d) In main: simulate a sequence of park/leave events, printing each outcome, then print final occupancy and revenue. Expected output: parking into a full section is refused; revenue = 20×(two parked) + 40×(four parked).

## Build and run

Requires JDK 17 or newer. Run these from the repository root.

### Task-2.1

```bash
cd Practical-02/Task-2.1
javac -d out $(find . -name '*.java')
java -cp out Thermostat
```

### Task-2.2

```bash
cd Practical-02/Task-2.2
javac -d out $(find . -name '*.java')
java -cp out CinemaShow
```

### Task-2.3

```bash
cd Practical-02/Task-2.3
javac -d out $(find . -name '*.java')
java -cp out ParkingLot
```
