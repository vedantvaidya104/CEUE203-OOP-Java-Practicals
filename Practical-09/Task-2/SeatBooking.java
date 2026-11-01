import java.util.concurrent.atomic.AtomicInteger;

public class SeatBooking {

    static class UnsafeTheatre {
        int seatsLeft = 5;

        boolean book() {
            if (seatsLeft > 0) {
                try {
                    Thread.sleep(5);   // simulates payment / processing delay
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                seatsLeft--;
                return true;
            }
            return false;
        }
    }

    static class SafeTheatre {
        int seatsLeft = 5;

        synchronized boolean book() {
            if (seatsLeft > 0) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                seatsLeft--;
                return true;
            }
            return false;
        }
    }

    interface Booker {
        boolean book();
    }

    static int tryBooking(Booker theatre, int users)
            throws InterruptedException {
        AtomicInteger success = new AtomicInteger();
        Thread[] threads = new Thread[users];
        for (int i = 0; i < users; i++) {
            threads[i] = new Thread(() -> {
                if (theatre.book()) {
                    success.incrementAndGet();
                }
            });
            threads[i].start();
        }
        for (Thread t : threads) {
            t.join();
        }
        return success.get();
    }

    public static void main(String[] args) throws InterruptedException {
        int users = 10;

        UnsafeTheatre unsafe = new UnsafeTheatre();
        int unsafeBooked = tryBooking(unsafe::book, users);
        System.out.println("Without synchronization: seats available = 5, "
                + "successful bookings = " + unsafeBooked
                + ", seatsLeft = " + unsafe.seatsLeft);

        SafeTheatre safe = new SafeTheatre();
        int safeBooked = tryBooking(safe::book, users);
        System.out.println("With synchronized book(): seats available = 5, "
                + "successful bookings = " + safeBooked
                + ", seatsLeft = " + safe.seatsLeft);
    }
}
