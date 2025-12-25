package com.library.controller;

import com.library.model.Book;
import com.library.repository.BookRep;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRep bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        List<Book> allBooks = bookRepository.findAll();
        for (Book book : allBooks) {
            bookRepository.delete(book.getId());
        }
    }

    @Test
    void testGetAllBooks() throws Exception {
        Book book = new Book("Книга", "Автор", 1869);
        bookRepository.save(book);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Книга"))
                .andExpect(jsonPath("$[0].author").value("Автор"));
    }

    @Test
    void testGetAllBooksEmpty() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetBookById() throws Exception {
        Book book = new Book("Книга", "Автор", 1949);
        bookRepository.save(book);

        List<Book> allBooks = bookRepository.findAll();
        Long bookId = allBooks.get(0).getId();

        mockMvc.perform(get("/api/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Книга"))
                .andExpect(jsonPath("$.author").value("Автор"))
                .andExpect(jsonPath("$.publicationYear").value(1949));
    }

    @Test
    void testCreateBook() throws Exception {
        Book newBook = new Book("Книга", "Автор", 1866);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Книга"))
                .andExpect(jsonPath("$.author").value("Автор"));
    }

    @Test
    void testCreateMultipleBooks() throws Exception {
        for (int i = 1; i <= 3; i++) {
            Book book = new Book("Книга " + i, "Автор " + i, 2020 + i);

            mockMvc.perform(post("/api/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(book)))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void testUpdateBook() throws Exception {
        Book book = new Book("Книга", "Автор", 2020);
        bookRepository.save(book);

        List<Book> allBooks = bookRepository.findAll();
        Long bookId = allBooks.get(0).getId();

        Book updated = new Book(bookId, "Новая книга", "Автор", 2020);

        mockMvc.perform(put("/api/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Новая книга"));
    }

    @Test
    void testUpdateBookAuthor() throws Exception {
        Book book = new Book("Книга", "Автор", 2020);
        bookRepository.save(book);

        List<Book> allBooks = bookRepository.findAll();
        Long bookId = allBooks.get(0).getId();

        Book updated = new Book(bookId, "Книга", "Новый Автор", 2020);

        mockMvc.perform(put("/api/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author").value("Новый Автор"));
    }

    @Test
    void testDeleteBook() throws Exception {
        Book book = new Book("Книга", "Автор", 2020);
        bookRepository.save(book);

        List<Book> allBooks = bookRepository.findAll();
        Long bookId = allBooks.get(0).getId();

        mockMvc.perform(delete("/api/books/" + bookId))
                .andExpect(status().isNoContent());

        List<Book> afterDelete = bookRepository.findAll();
        assert afterDelete.isEmpty();
    }

    @Test
    void testDeleteNonExistentBook() throws Exception {
        mockMvc.perform(delete("/api/books/999"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testContentTypeJson() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}