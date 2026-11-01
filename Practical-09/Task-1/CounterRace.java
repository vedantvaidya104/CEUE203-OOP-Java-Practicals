public class CounterRace {

    static int unsafeCount = 0;
    static int safeCount = 0;

    static void unsafeIncrement(int i) {
        int temp = unsafeCount;          // read
        if (i % 10000 == 0) {
            Thread.yield();              // let another thread run in between
        }
        unsafeCount = temp + 1;          // write (may overwrite an update)
    }

    static synchronized void safeIncrement() {
        safeCount++;
    }

    static void runThreads(Runnable task, int threads)
            throws InterruptedException {
        Thread[] arr = new Thread[threads];
        for (int i = 0; i < threads; i++) {
            arr[i] = new Thread(task);
            arr[i].start();
        }
        for (Thread t : arr) {
            t.join();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int threads = 10;
        int perThread = 100000;
        int expected = threads * perThread;

        for (int run = 1; run <= 3; run++) {
            unsafeCount = 0;
            safeCount = 0;

            runThreads(() -> {
                for (int i = 0; i < perThread; i++) unsafeIncrement(i);
            }, threads);

            runThreads(() -> {
                for (int i = 0; i < perThread; i++) safeIncrement();
            }, threads);

            System.out.println("Run " + run + " | expected = " + expected
                    + " | without sync = " + unsafeCount
                    + " | with sync = " + safeCount);
        }
    }
}
