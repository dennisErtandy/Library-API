package com.example.demo;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface BookService {

  Flux<Book> getAllBooks();

  Mono<Book> getBookById(String id);

  Mono<Book> getBookByIsbn(String isbn);

  Mono<Book> create(CreateBookRequest createBookRequest);

  Mono<Book> deleteBook(String id);

  Mono<Book> updateBook(String id, CreateBookRequest createBookRequest);

}
