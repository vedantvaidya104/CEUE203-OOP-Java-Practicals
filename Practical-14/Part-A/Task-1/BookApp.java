import java.util.Scanner;

public class BookApp {
    public static void main(String[] args) {
        BookService service = new BookService();
        Scanner in = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n1. Add book  2. List books  0. Exit");
            System.out.print("Choice: ");
            String choice = in.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> {
                        System.out.print("Title: ");
                        String title = in.nextLine();
                        System.out.print("Author: ");
                        service.addBook(title, in.nextLine());
                        System.out.println("Book added. Total = " + service.count());
                    }
                    case "2" -> service.listBooks().forEach(System.out::println);
                    case "0" -> running = false;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }
}
