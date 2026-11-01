import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BookServiceTest {

    @Test
    void addingBookIncreasesCount() {
        BookService service = new BookService();
        service.addBook("Wings of Fire", "A.P.J. Abdul Kalam");
        service.addBook("Clean Code", "Robert Martin");
        assertEquals(2, service.count());
    }

    @Test
    void blankTitleThrowsException() {
        BookService service = new BookService();
        assertThrows(IllegalArgumentException.class,
            () -> service.addBook("  ", "Someone"));
    }

    @Test
    void failedAddDoesNotChangeCount() {
        BookService service = new BookService();
        assertThrows(IllegalArgumentException.class,
            () -> service.addBook("Title", ""));
        assertEquals(0, service.count());
    }
}
