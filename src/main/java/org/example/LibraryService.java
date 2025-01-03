package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Book {
    private String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private boolean isAvailable;

    public Book(String isbn, String title, String author, int publicationYear) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.isAvailable = true;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}

public class LibraryService {
    private final Map<String, Book> bookMap = new HashMap<>();

    public void addBook(Book book) throws Exception {
        if (book == null || book.getIsbn() == null || book.getIsbn().isEmpty() ||
                book.getTitle() == null || book.getTitle().isEmpty() ||
                book.getAuthor() == null || book.getAuthor().isEmpty()) {
            throw new Exception("Book details cannot be null or empty.");
        }

        if (bookMap.containsKey(book.getIsbn())) {
            throw new Exception("Book with this ISBN already exists.");
        }

        bookMap.put(book.getIsbn(), book);
    }

    public String borrowBook(String isbn) throws Exception {
        if (isbn == null || isbn.isEmpty()) {
            throw new Exception("Book details cannot be null or empty.");
        }

        Book book = bookMap.get(isbn);
        if (book == null || !book.isAvailable()) {
            throw new Exception("Book is currently unavailable");
        }

        book.setAvailable(false);
        return "Book successfully borrowed";
    }

    public String returnBook(String isbn) throws Exception {
        if (isbn == null || isbn.isEmpty()) {
            throw new Exception("Book details cannot be null or empty.");
        }

        Book book = bookMap.get(isbn);
        if (book == null) {
            throw new Exception("Book not found");
        }

        if (book.isAvailable()) {
            throw new Exception("Book was not borrowed");
        }

        book.setAvailable(true);
        return "Book successfully returned";
    }

    public List<Book> viewAvailableBooks() {
        return bookMap.values().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }
}
