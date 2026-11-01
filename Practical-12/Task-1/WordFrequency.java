import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequency {

    public static void main(String[] args) {
        String paragraph = "Java is fun and Java is powerful. "
                + "Learning Java is easy when practice is regular, "
                + "and practice makes Java programming fun.";

        // Split into lowercase words (ignore punctuation)
        String[] words = paragraph.toLowerCase().split("[^a-z]+");

        Map<String, Integer> freq = new HashMap<>();
        for (String w : words) {
            if (!w.isEmpty()) {
                freq.put(w, freq.getOrDefault(w, 0) + 1);
            }
        }

        // Sort entries by frequency (highest first); ties by word
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(freq.entrySet());
        entries.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> a,
                               Map.Entry<String, Integer> b) {
                int byCount = Integer.compare(b.getValue(), a.getValue());
                return byCount != 0 ? byCount : a.getKey().compareTo(b.getKey());
            }
        });

        System.out.println("Word frequency (highest first):");
        for (Map.Entry<String, Integer> e : entries) {
            System.out.printf("  %-12s %d%n", e.getKey(), e.getValue());
        }
    }
}
