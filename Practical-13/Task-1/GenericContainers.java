public class GenericContainers {

    // Generic method: returns the first element of any array
    static <T> T first(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[0];
    }

    public static void main(String[] args) {
        Box<String> nameBox = new Box<>();
        nameBox.set("MiniBank");
        String name = nameBox.get();          // no cast needed
        System.out.println("Box<String>  : " + name);

        Box<Integer> numberBox = new Box<>();
        numberBox.set(2026);
        int year = numberBox.get();           // no cast needed
        System.out.println("Box<Integer> : " + year);

        // nameBox.set(100);  // compile-time error: type safety

        Pair<String, Integer> p1 = new Pair<>("Riya", 86);
        Pair<Integer, Double> p2 = new Pair<>(101, 4500.75);
        System.out.println("Pair<String,Integer> : " + p1);
        System.out.println("Pair<Integer,Double> : " + p2);

        String[] names = {"Riya", "Karan", "Meera"};
        Integer[] nums = {10, 20, 30};
        Double[] empty = {};
        System.out.println("first(String[])  : " + first(names));
        System.out.println("first(Integer[]) : " + first(nums));
        System.out.println("first(empty)     : " + first(empty));
    }
}

class Box<T> {
    private T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}

class Pair<A, B> {
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
