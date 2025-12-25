package com.library.service;

import com.library.model.Book;
import com.library.repository.BookRep;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRep repository;

    public BookService(BookRep repository) {
        this.repository = repository;
    }

    public void createBook(Book book){
        if (book.getAuthor() == null || book.getTitle() == null ||
                book.getPublicationYear() <=1000 ||
                book.getPublicationYear() > LocalDateTime.now().getYear()){
            throw new IllegalArgumentException("Некорректные данные книги");
        }
        repository.save(book);
    }

    public Book getBookById(Long id){
        Optional<Book> book = repository.findById(id);
        if (book.isPresent()){
            System.out.println(book.toString());
            return book.get();
        }else {
            System.out.println("Книга не найдена");
            return null;
        }
    }

    public List<Book> getAllBooks(){
        return repository.findAll();
    }

    public void updateBook(Book book){
        if (repository.findById(book.getId()).isEmpty()){
            throw new IllegalArgumentException("Нет книги с таким id");
        }

        repository.update(book);
    }

    public void deleteBook(Long id){
        repository.delete(id);
    }
}
