package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class BookServiceImpl implements BookService {

  @Autowired
  private BookRepository bookRepository;

  @Override
  public Flux<Book> getAllBooks() {
    return bookRepository.getAllBooks();
  }

  public Mono<Book> getBookById(String id) {
    return bookRepository.findBookById(id);
  }

  public Mono<Book> getBookByIsbn(String isbn) {
    return bookRepository.findBookByIsbn(isbn);
  }

  public Mono<Book> create(CreateBookRequest createBookRequest) {
    final Book book = new Book().toBuilder().title(createBookRequest.getTitle())
        .isbn(createBookRequest.getIsbn()).price(createBookRequest.getPrice()).build();
    return bookRepository.insert(book);
  }

  public Mono<Book> deleteBook(String id) {
    return bookRepository.deleteBookById(id);
  }

  public Mono<Book> updateBook(String id, CreateBookRequest createBookRequest) {
    return bookRepository.updateBookById(id, createBookRequest);
  }

}
