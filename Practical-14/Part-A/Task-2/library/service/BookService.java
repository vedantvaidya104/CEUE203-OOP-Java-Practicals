package library.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import library.model.Book;

public class BookService {
    private List<Book> books = new ArrayList<>();

    public void addBook(String title, String author) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title must not be empty");
        }
        books.add(new Book(title.trim(), author.trim()));
    }

    public List<Book> listBooks() { return new ArrayList<>(books); }

    public void save(Path file) throws IOException {
        try (ObjectOutputStream out =
                new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(new ArrayList<>(books));
        }
    }

    @SuppressWarnings("unchecked")
    public void load(Path file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in =
                new ObjectInputStream(Files.newInputStream(file))) {
            books = (List<Book>) in.readObject();
        }
    }
}
