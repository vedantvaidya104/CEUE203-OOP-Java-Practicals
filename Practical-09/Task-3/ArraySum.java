import java.util.concurrent.atomic.AtomicLong;

public class ArraySum {

    static long unsafeTotal = 0;
    static long syncTotal = 0;
    static final Object lock = new Object();
    static final AtomicLong atomicTotal = new AtomicLong();

    interface Worker {
        void process(int[] data, int from, int to);
    }

    static long timeIt(int[] data, int threads, Worker w)
            throws InterruptedException {
        long start = System.nanoTime();
        Thread[] arr = new Thread[threads];
        int chunk = data.length / threads;
        for (int i = 0; i < threads; i++) {
            final int from = i * chunk;
            final int to = (i == threads - 1) ? data.length : from + chunk;
            arr[i] = new Thread(() -> w.process(data, from, to));
            arr[i].start();
        }
        for (Thread t : arr) {
            t.join();
        }
        return System.nanoTime() - start;
    }

    public static void main(String[] args) throws InterruptedException {
        int size = 2_000_000;
        int threads = 4;
        int[] data = new int[size];
        long expected = 0;
        for (int i = 0; i < size; i++) {
            data[i] = (i % 10) + 1;
            expected += data[i];
        }
        System.out.println("Expected total        : " + expected);

        // 1. No protection -> wrong result
        long t1 = timeIt(data, threads, (d, from, to) -> {
            for (int i = from; i < to; i++) unsafeTotal += d[i];
        });
        System.out.println("No synchronization    : " + unsafeTotal
                + "  (" + t1 / 1_000_000 + " ms)  -> WRONG");

        // 2. Fix one: synchronized block on every addition
        long t2 = timeIt(data, threads, (d, from, to) -> {
            for (int i = from; i < to; i++) {
                synchronized (lock) {
                    syncTotal += d[i];
                }
            }
        });
        System.out.println("synchronized block    : " + syncTotal
                + "  (" + t2 / 1_000_000 + " ms)  -> correct");

        // 3. Fix two: local sums per thread, then one add to an AtomicLong
        long t3 = timeIt(data, threads, (d, from, to) -> {
            long local = 0;
            for (int i = from; i < to; i++) local += d[i];
            atomicTotal.addAndGet(local);
        });
        System.out.println("Local sum + AtomicLong: " + atomicTotal.get()
                + "  (" + t3 / 1_000_000 + " ms)  -> correct");
    }
}
