import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UniqueVisitors {

    public static void main(String[] args) {
        List<String> logins = Arrays.asList(
                "riya", "karan", "riya", "meera", "karan",
                "arjun", "meera", "riya", "neha");

        System.out.println("All logins     : " + logins);
        System.out.println("Total logins   : " + logins.size());

        Set<String> uniqueUsers = new HashSet<>(logins);
        System.out.println("Unique users   : " + uniqueUsers.size());

        // LinkedHashSet keeps first-seen order
        Set<String> ordered = new LinkedHashSet<>(logins);
        System.out.println("First-seen order: " + ordered);

        List<String> firstThree = new ArrayList<>(ordered).subList(0, 3);
        System.out.println("First three distinct names: " + firstThree);
    }
}
