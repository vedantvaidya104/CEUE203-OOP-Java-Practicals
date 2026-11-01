package library;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;
import library.service.BookService;
import org.junit.jupiter.api.Test;

class BookServiceTest {

    @Test
    void saveAndReloadKeepsDataUnchanged() throws Exception {
        BookService original = new BookService();
        original.addBook("Wings of Fire", "A.P.J. Abdul Kalam");
        original.addBook("Clean Code", "Robert Martin");

        Path file = Files.createTempFile("books", ".dat");
        original.save(file);

        BookService reloaded = new BookService();
        reloaded.load(file);

        assertEquals(2, reloaded.listBooks().size());
        assertEquals("Wings of Fire", reloaded.listBooks().get(0).getTitle());
        assertEquals("Clean Code", reloaded.listBooks().get(1).getTitle());
        Files.deleteIfExists(file);
    }
}
