import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leaderboard {

    static void printTop(Map<String, Integer> scores, int n) {
        List<Map.Entry<String, Integer>> list = new ArrayList<>(scores.entrySet());

        // Higher score first; ties broken alphabetically by name
        list.sort((a, b) -> {
            int cmp = Integer.compare(b.getValue(), a.getValue());
            return cmp != 0 ? cmp : a.getKey().compareTo(b.getKey());
        });

        System.out.println("Top " + n + " players:");
        for (int i = 0; i < Math.min(n, list.size()); i++) {
            Map.Entry<String, Integer> e = list.get(i);
            System.out.printf("  %d. %-8s %d%n", i + 1, e.getKey(), e.getValue());
        }
    }

    public static void main(String[] args) {
        Map<String, Integer> scores = new HashMap<>();
        scores.put("Riya", 850);
        scores.put("Karan", 920);
        scores.put("Meera", 850);
        scores.put("Arjun", 700);
        scores.put("Neha", 920);
        scores.put("Dev", 610);

        printTop(scores, 3);
        System.out.println();
        printTop(scores, 10);   // N larger than players: prints everyone
    }
}
