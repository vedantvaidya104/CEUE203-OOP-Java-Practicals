package library;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import library.service.BookService;

public class LibraryApp {
    public static void main(String[] args) throws Exception {
        Path file = Paths.get("books.dat");
        BookService service = new BookService();
        if (Files.exists(file)) {
            service.load(file);
            System.out.println("Loaded from file:");
            service.listBooks().forEach(b -> System.out.println("  " + b));
        } else {
            System.out.println("No saved data found. Adding sample books.");
            service.addBook("Wings of Fire", "A.P.J. Abdul Kalam");
            service.addBook("Clean Code", "Robert Martin");
            service.save(file);
            System.out.println("Saved " + service.listBooks().size() + " books.");
        }
    }
}
