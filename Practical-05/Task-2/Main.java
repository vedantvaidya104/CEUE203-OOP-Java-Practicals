abstract class Employee {
    String name;
    int id;

    Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    abstract double monthlySalary();
}

class FullTime extends Employee {
    double fixedSalary;

    FullTime(String name, int id, double fixedSalary) {
        super(name, id);
        this.fixedSalary = fixedSalary;
    }

    double monthlySalary() {
        return fixedSalary;
    }
}

class PartTime extends Employee {
    double hours;
    double rate;

    PartTime(String name, int id, double hours, double rate) {
        super(name, id);
        this.hours = hours;
        this.rate = rate;
    }

    double monthlySalary() {
        return hours * rate;
    }
}

class Intern extends Employee {
    double stipend;

    Intern(String name, int id, double stipend) {
        super(name, id);
        this.stipend = stipend;
    }

    double monthlySalary() {
        return stipend;
    }
}

public class Main {
    public static void main(String[] args) {
        Employee[] employees = {
            new FullTime("Alice", 101, 5000.0),
            new PartTime("Bob", 102, 80.0, 20.0),
            new Intern("Charlie", 103, 1500.0)
        };

        double totalSalary = 0.0;

        for (Employee e : employees) {
            double salary = e.monthlySalary();
            System.out.print(e.id + " " + e.name + " - " + salary);

            if (e instanceof Intern) {
                System.out.print(" (Internship Stipend)");
            }
            System.out.println();
            totalSalary += salary;
        }

        System.out.println("Total monthly payroll = " + totalSalary);
    }
}
