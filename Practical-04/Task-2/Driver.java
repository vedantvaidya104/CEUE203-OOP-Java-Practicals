import java.util.Scanner;

public class Driver {

    public static void main(String[] args) {

        String[] logs = {
            "10:05 alice Hello there",
            "10:10 bob How are you?",
            "MalformedLine",
            "10:15 charlie hello everyone"
        };

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter keyword: ");
        String keyword = sc.nextLine();

        ChatFilter cf = new ChatFilter();

        System.out.println(cf.filterChats(logs, keyword));

        sc.close();
    }
}
