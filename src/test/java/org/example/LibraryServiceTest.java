package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryServiceTest {

    @Test
    public void testAddValidBook() throws Exception {
        LibraryService library = new LibraryService();
        Book book = new Book("1234", "Java Basics", "John Doe", 2020);

        assertDoesNotThrow(() -> library.addBook(book));
    }

    @Test
    public void testAddDuplicateBook() throws Exception {
        LibraryService library = new LibraryService();
        Book book1 = new Book("1234", "Java Basics", "John Doe", 2020);
        Book book2 = new Book("1234", "Java Basics", "John Doe", 2020);

        library.addBook(book1); // First addition should succeed
        Exception exception = assertThrows(Exception.class, () -> library.addBook(book2)); // Second should fail

        assertEquals("Book with this ISBN already exists.", exception.getMessage());
    }

    @Test
    public void testAddBookWithMissingISBN() {
        LibraryService library = new LibraryService();

        Exception exception = assertThrows(Exception.class, () -> {
            library.addBook(new Book(null, "Java Basics", "John Doe", 2020));
        });
        assertEquals("Book details cannot be null or empty.", exception.getMessage());

        exception = assertThrows(Exception.class, () -> {
            library.addBook(new Book("", "Java Basics", "John Doe", 2020));
        });
        assertEquals("Book details cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testAddBookWithMissingTitle() {
        LibraryService library = new LibraryService();

        Exception exception = assertThrows(Exception.class, () -> {
            library.addBook(new Book("1234", null, "John Doe", 2020));
        });
        assertEquals("Book details cannot be null or empty.", exception.getMessage());

        exception = assertThrows(Exception.class, () -> {
            library.addBook(new Book("1234", "", "John Doe", 2020));
        });
        assertEquals("Book details cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testAddBookWithMissingAuthor() {
        LibraryService library = new LibraryService();

        Exception exception = assertThrows(Exception.class, () -> {
            library.addBook(new Book("1234", "Java Basics", null, 2020));
        });
        assertEquals("Book details cannot be null or empty.", exception.getMessage());

        exception = assertThrows(Exception.class, () -> {
            library.addBook(new Book("1234", "Java Basics", "", 2020));
        });
        assertEquals("Book details cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testSuccessfulBorrowing() throws Exception {
        LibraryService library = new LibraryService();
        Book book = new Book("1234", "Java Basics", "John Doe", 2020);
        library.addBook(book);

        String result = library.borrowBook(book.getIsbn());

        assertEquals("Book successfully borrowed", result);
    }

    @Test
    public void testBookAlreadyBorrowed() throws Exception {
        LibraryService library = new LibraryService();
        Book book = new Book("1234", "Java Basics", "John Doe", 2020);
        library.addBook(book);

        library.borrowBook(book.getIsbn());

        Exception exception = assertThrows(Exception.class, () -> {
            library.borrowBook(book.getIsbn());
        });
        assertEquals("Book is currently unavailable", exception.getMessage());
    }

    @Test
    public void testBookNotAvailable() throws Exception {
        LibraryService library = new LibraryService();
        Book book = new Book("1234", "Java Basics", "John Doe", 2020);
        library.addBook(book);

        library.borrowBook(book.getIsbn());

        Exception exception = assertThrows(Exception.class, () -> {
            library.borrowBook(book.getIsbn());
        });
        assertEquals("Book is currently unavailable", exception.getMessage());
    }

    @Test
    public void testBorrowBookWithMissingDetails() throws Exception {
        LibraryService library = new LibraryService();

        Exception exception = assertThrows(Exception.class, () -> {
            library.borrowBook(null);
        });
        assertEquals("Book details cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testSuccessfulReturn() throws Exception {
        LibraryService library = new LibraryService();
        Book book = new Book("1234", "Java Basics", "John Doe", 2020);
        library.addBook(book);

        library.borrowBook(book.getIsbn());

        String result = library.returnBook(book.getIsbn());

        assertEquals("Book successfully returned", result);
        assertTrue(book.isAvailable());
    }

    @Test
    public void testBookNotBorrowed() throws Exception {
        LibraryService library = new LibraryService();
        Book book = new Book("1234", "Java Basics", "John Doe", 2020);
        library.addBook(book);

        Exception exception = assertThrows(Exception.class, () -> {
            library.returnBook(book.getIsbn());
        });

        assertEquals("Book was not borrowed", exception.getMessage());
    }

    @Test
    public void testReturnBookWithMissingIsbn() throws Exception {
        LibraryService library = new LibraryService();

        Exception exception = assertThrows(Exception.class, () -> {
            library.returnBook(null);
        });
        assertEquals("Book details cannot be null or empty.", exception.getMessage());

        exception = assertThrows(Exception.class, () -> {
            library.returnBook("");
        });
        assertEquals("Book details cannot be null or empty.", exception.getMessage());
    }

    @Test
    public void testReturnBookThatDoesNotExist() throws Exception {
        LibraryService library = new LibraryService();
        Book book = new Book("1234", "Java Basics", "John Doe", 2020);

        Exception exception = assertThrows(Exception.class, () -> {
            library.returnBook(book.getIsbn());
        });

        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    public void testViewAllAvailableBooks() throws Exception {
        LibraryService library = new LibraryService();
        Book book1 = new Book("1234", "Java Basics", "John Doe", 2020);
        Book book2 = new Book("5678", "Python Basics", "Jane Doe", 2021);

        library.addBook(book1);
        library.addBook(book2);

        List<Book> availableBooks = library.viewAvailableBooks();

        assertEquals(2, availableBooks.size());
        assertTrue(availableBooks.contains(book1));
        assertTrue(availableBooks.contains(book2));
    }

    @Test
    public void testNoBooksAvailable() throws Exception {
        LibraryService library = new LibraryService();

        List<Book> availableBooks = library.viewAvailableBooks();

        assertEquals(0, availableBooks.size());
    }

    @Test
    public void testExcludeBorrowedBooks() throws Exception {
        LibraryService library = new LibraryService();
        Book book1 = new Book("1234", "Java Basics", "John Doe", 2020);
        Book book2 = new Book("5678", "Python Basics", "Jane Doe", 2021);

        library.addBook(book1);
        library.addBook(book2);

        library.borrowBook(book1.getIsbn());

        List<Book> availableBooks = library.viewAvailableBooks();

        assertEquals(1, availableBooks.size());
        assertTrue(availableBooks.contains(book2));
        assertFalse(availableBooks.contains(book1));
    }

}
