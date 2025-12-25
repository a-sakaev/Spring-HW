package com.library.repository;

import com.library.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRep {

    private final JdbcTemplate jdbcTemplate;

    public void save(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Не указана книга для сохранения");
        }

        jdbcTemplate.update(
                "INSERT INTO books (title, author, publication_year) VALUES (?, ?, ?)",
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear()

        );

        System.out.println("Книга сохранена");

    }

    public Optional<Book> findById(Long id) {
        try {
            Book book = jdbcTemplate.queryForObject(
                    "SELECT id, title, author, publication_year FROM books WHERE id = ?",
                    (rs, rowNum) -> {
                        Long bookId = rs.getLong("id");
                        String bookTitle = rs.getString("title");
                        String bookAuthor = rs.getString("author");
                        Integer bookYear = rs.getInt("publication_year");

                        return new Book(bookId, bookTitle, bookAuthor, bookYear);
                    },
                    id
            );
            return Optional.of(book);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    public List<Book> findAll() {
        return jdbcTemplate.query(
                "SELECT id, title, author, publication_year FROM books",
                (rs, rowNum) -> {
                    Long bookId = rs.getLong("id");
                    String bookTitle = rs.getString("title");
                    String bookAuthor = rs.getString("author");
                    Integer bookYear = rs.getInt("publication_year");

                    return new Book(bookId, bookTitle, bookAuthor, bookYear);
                }
        );
    }

    public void update(Book book) {
        if (book == null || book.getId() == null) {
            throw new IllegalArgumentException("Неверные данные");
        }

        int rowAffect = jdbcTemplate.update(
                "UPDATE books SET title = ?, author = ?, publication_year = ? WHERE ID = ?",
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear(),
                book.getId()
        );

        if (rowAffect == 0) {
            throw new IllegalArgumentException("Книга не найдена");
        } else {
            System.out.println("Книга обновлена");
        }


    }

    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Не указан id");
        }

        int rowAffect = jdbcTemplate.update(
                "DELETE FROM books WHERE id = ?",
                id
        );

        if (rowAffect == 0) {
            throw new IllegalArgumentException("Книга не найдена");
        } else {
            System.out.println("Книга удалена");
        }
    }

}
