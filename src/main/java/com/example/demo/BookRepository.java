package com.example.demo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface BookRepository extends CommonDataRepository<Book> {

  Mono<Book> findBookByIsbn(String isbn);

  Flux<Book> getAllBooks();

  Mono<Book> findBookById(String id);

  Mono<Book> deleteBookById(String id);

  Mono<Book> updateBookById(String id, CreateBookRequest createBookRequest);

}
