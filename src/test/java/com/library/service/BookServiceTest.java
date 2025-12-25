package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService service;

    @Autowired
    private BookRep bookRepository;

    @BeforeEach
    void setUp() {
        List<Book> allBooks = bookRepository.findAll();
        for (Book book : allBooks) {
            bookRepository.delete(book.getId());
        }
    }

    @Test
    void testSaveBook() {
        Book book = new Book("Книга", "Автор", 1869);
        Book saved = service.createBook(book);

        assertNotNull(saved);
        assertEquals("Книга", saved.getTitle());
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book("Книга1", "Автор1", 2000);
        Book book2 = new Book("Книга2", "Автор2", 2001);

        service.createBook(book1);
        service.createBook(book2);

        List<Book> books = service.getAllBooks();
        assertEquals(2, books.size());
    }

    @Test
    void testGetAllBooksEmpty() {
        List<Book> books = service.getAllBooks();
        assertTrue(books.isEmpty());
    }

    @Test
    void testGetBookById() {
        Book book = new Book("Книга", "Автор", 1949);
        service.createBook(book);

        List<Book> allBooks = service.getAllBooks();
        Long bookId = allBooks.get(0).getId();

        Book found = service.getBookById(bookId).get();
        assertNotNull(found);
        assertEquals("Книга", found.getTitle());
        assertEquals("Автор", found.getAuthor());
    }

    @Test
    void testUpdateBook() {
        Book book = new Book("Книга", "Автор", 2020);
        service.createBook(book);

        List<Book> allBooks = service.getAllBooks();
        Book bookToUpdate = allBooks.get(0);
        bookToUpdate.setTitle("Обновленная книга");

        service.updateBook(bookToUpdate);

        Book updated = service.getBookById(bookToUpdate.getId()).get();
        assertEquals("Updated Title", updated.getTitle());
    }

    @Test
    void testDeleteBook() {
        Book book = new Book("Обновленная книга", "Автор", 2020);
        service.createBook(book);

        List<Book> allBooks = service.getAllBooks();
        Long bookId = allBooks.get(0).getId();

        service.deleteBook(bookId);

        List<Book> afterDelete = service.getAllBooks();
        assertTrue(afterDelete.isEmpty());
    }

    @Test
    void testSaveMultipleBooks() {
        for (int i = 0; i < 5; i++) {
            Book book = new Book("Книга " + i, "Автор " + i, 2020 + i);
            service.createBook(book);
        }

        List<Book> books = service.getAllBooks();
        assertEquals(5, books.size());
    }

    @Test
    void testDeleteAllBooks() {
        Book book1 = new Book("Книга1", "Автор1", 2000);
        Book book2 = new Book("Книга2", "Автор2", 2001);

        service.createBook(book1);
        service.createBook(book2);

        List<Book> allBooks = service.getAllBooks();
        for (Book book : allBooks) {
            service.deleteBook(book.getId());
        }

        List<Book> afterDelete = service.getAllBooks();
        assertTrue(afterDelete.isEmpty());
    }
}


