public class BoundedStats {

    // N must be a Number (Integer, Double, Long, ...)
    static <N extends Number> double average(N[] values) {
        if (values.length == 0) {
            return 0;
        }
        double sum = 0;
        for (N v : values) {
            sum += v.doubleValue();   // available because N extends Number
        }
        return sum / values.length;
    }

    public static void main(String[] args) {
        Integer[] ints = {10, 20, 30, 40};
        Double[] doubles = {1.5, 2.5, 3.5};
        Long[] longs = {100L, 200L, 300L};

        System.out.println("average(Integer[]) = " + average(ints));
        System.out.println("average(Double[])  = " + average(doubles));
        System.out.println("average(Long[])    = " + average(longs));

        // String[] words = {"a", "b"};
        // average(words);   // compile error: String is not a Number
        System.out.println("average(String[]) is rejected at compile time "
                + "because String is not a Number.");
    }
}
