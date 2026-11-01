import java.util.ArrayList;
import java.util.List;

public class BookService {
    private final List<Book> books = new ArrayList<>();

    public void addBook(String title, String author) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title must not be empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author must not be empty");
        }
        books.add(new Book(title.trim(), author.trim()));
    }

    public int count() {
        return books.size();
    }

    public List<Book> listBooks() {
        return new ArrayList<>(books);
    }
}
