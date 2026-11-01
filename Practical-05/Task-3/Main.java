abstract class Media {
    String title;
    int daysLate;

    Media(String title, int daysLate) {
        this.title = title;
        this.daysLate = daysLate;
    }

    abstract double lateFee();
}

class Book extends Media {
    Book(String title, int daysLate) {
        super(title, daysLate);
    }

    // Rs. 2 per day
    double lateFee() {
        return daysLate * 2.0;
    }
}

class DVD extends Media {
    DVD(String title, int daysLate) {
        super(title, daysLate);
    }

    // Rs. 5 per day, capped at Rs. 50
    double lateFee() {
        return Math.min(daysLate * 5.0, 50.0);
    }
}

class Magazine extends Media {
    Magazine(String title, int daysLate) {
        super(title, daysLate);
    }

    // First 3 days free, then Rs. 1 per day
    double lateFee() {
        return daysLate > 3 ? (daysLate - 3) * 1.0 : 0.0;
    }
}

public class Main {
    public static void main(String[] args) {
        Media[] returned = {
            new Book("Java: The Complete Reference", 5),
            new DVD("Inception", 12),
            new Magazine("National Geographic", 8),
            new Book("Clean Code", 0)
        };

        double total = 0.0;

        for (Media m : returned) {
            double fee = m.lateFee();
            System.out.println(m.getClass().getSimpleName() + " \"" + m.title
                + "\" (" + m.daysLate + " days late) -> Rs. " + fee);
            total += fee;
        }

        System.out.println("Total late fees = Rs. " + total);
    }
}
