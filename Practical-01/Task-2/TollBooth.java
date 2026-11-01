import java.util.Scanner;

public class TollBooth {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int totalToll = 0;
        int bike = 0, car = 0, truck = 0;

        while (true) {
            System.out.print("Vehicle number (or 'done'): ");
            String number = sc.next();
            if (number.equalsIgnoreCase("done")) {
                break;
            }
            System.out.print("Vehicle type (bike/car/truck): ");
            Vehicle v = new Vehicle(number, sc.next().toLowerCase());

            int toll = switch (v.type()) {
                case "bike"  -> 20;
                case "car"   -> 50;
                case "truck" -> 150;
                default      -> 0;
            };

            totalToll += toll;
            switch (v.type()) {
                case "bike"  -> bike++;
                case "car"   -> car++;
                case "truck" -> truck++;
                default      -> { }
            }
        }

        String mostFrequent = "bike";
        int max = bike;
        if (car > max)   { max = car;   mostFrequent = "car"; }
        if (truck > max) { max = truck; mostFrequent = "truck"; }

        System.out.println("Total toll: " + totalToll);
        System.out.println("Most frequent: " + mostFrequent);
        sc.close();
    }
}

record Vehicle(String number, String type) { }
