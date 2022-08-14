package com.example.demo;

import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.Arrays;
import java.util.Collections;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.modelmapper.ModelMapper;


@Slf4j
@RestController
@RequestMapping(value = "/api")
public class BookController {

  private static final String SUCCESS_CODE = "200";

  private static final String BOOK_ENDPOINT = "/books";
  private static final String ISBN_ENDPOINT = "/isbn/{isbn}";
  private static final String ID_ENDPOINT = "/id/{id}";

  @Autowired
  private BookService bookService;
  @Autowired
  private ModelMapper modelMapper;


  @GetMapping(value = BOOK_ENDPOINT, produces = {MediaType.APPLICATION_JSON_VALUE,})
  public Mono<ResponseEntity> getBooks() {
    return bookService.getAllBooks().switchIfEmpty(Mono.defer(Mono::empty))
        .map(book -> modelMapper.map(book, BookResponse.class))
        .collectList()
        .map(bookResponses -> new ResponseTemplate(ResponseTemplate.SUCCESS_STATUS,
            ResponseTemplate.SUCCESS_RETRIEVE_MESSAGE, bookResponses))
        .map(bookResponses -> ResponseEntity.status(HttpStatus.OK).body(bookResponses));
  }

  @PostMapping(value = BOOK_ENDPOINT, produces = {MediaType.APPLICATION_JSON_VALUE,})
  public Mono<ResponseEntity> createBooks(@Valid @RequestBody CreateBookRequest createBookRequest) {
    return bookService.create(createBookRequest)
        .map(book -> modelMapper.map(book, BookResponse.class))
        .map(bookResponse -> new ResponseTemplate(ResponseTemplate.SUCCESS_STATUS,
            ResponseTemplate.SUCCESS_CREATE_MESSAGE, Collections.singletonList(bookResponse)))
        .map(bookResponses -> ResponseEntity.status(HttpStatus.OK).body(bookResponses));
  }

  @GetMapping(value = BOOK_ENDPOINT + ISBN_ENDPOINT, produces = {MediaType.APPLICATION_JSON_VALUE,})
  public Mono<ResponseEntity> getBooksByIsbn(@PathVariable(value = "isbn") String isbn) {

    return bookService.getBookByIsbn(isbn)
        .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
            ResponseTemplate.FAILED_UPDATE_MESSAGE))))
        .map(book -> modelMapper.map(book, BookResponse.class))
        .map(bookResponse -> new ResponseTemplate(ResponseTemplate.SUCCESS_STATUS,
            ResponseTemplate.SUCCESS_RETRIEVE_MESSAGE, Collections.singletonList(bookResponse))
        )
        .map(bookResponses -> ResponseEntity.status(HttpStatus.OK).body(bookResponses));
  }

  @GetMapping(value = BOOK_ENDPOINT + ID_ENDPOINT, produces = {MediaType.APPLICATION_JSON_VALUE,})
  public Mono<ResponseEntity> getBooksById(@PathVariable(value = "id") String id) {

    return bookService.getBookById(id)
        .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
            ResponseTemplate.FAILED_UPDATE_MESSAGE))))
        .map(book -> modelMapper.map(book, BookResponse.class))
        .map(bookResponse -> new ResponseTemplate(ResponseTemplate.SUCCESS_STATUS,
            ResponseTemplate.SUCCESS_RETRIEVE_MESSAGE, Collections.singletonList(bookResponse))
        )
        .map(bookResponses -> ResponseEntity.status(HttpStatus.OK).body(bookResponses));
  }

  @DeleteMapping(value = BOOK_ENDPOINT + ID_ENDPOINT, produces = {
      MediaType.APPLICATION_JSON_VALUE,})
  public Mono<ResponseEntity> deleteBooks(@PathVariable(value = "id") String id) {

    return bookService.deleteBook(id)
        .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
            ResponseTemplate.FAILED_UPDATE_MESSAGE))))
        .map(book -> modelMapper.map(book, BookResponse.class))
        .map(bookResponse -> new ResponseTemplate(ResponseTemplate.SUCCESS_STATUS,
            ResponseTemplate.SUCCESS_DELETE_MESSAGE, Collections.singletonList(bookResponse))
        )
        .map(bookResponses -> ResponseEntity.status(HttpStatus.OK).body(bookResponses));
  }

  @PutMapping(value = BOOK_ENDPOINT + ID_ENDPOINT, produces = {
      MediaType.APPLICATION_JSON_VALUE,})
  public Mono<ResponseEntity> updateBooks(@Valid @RequestBody CreateBookRequest createBookRequest,
      @PathVariable(value = "id") String id) {

    return bookService.updateBook(id, createBookRequest)
        .switchIfEmpty(Mono.defer(() -> Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
            ResponseTemplate.FAILED_UPDATE_MESSAGE))))
        .map(book -> modelMapper.map(book, BookResponse.class))
        .map(bookResponse -> new ResponseTemplate(ResponseTemplate.SUCCESS_STATUS,
            ResponseTemplate.SUCCESS_UPDATE_MESSAGE, Collections.singletonList(bookResponse))
        )
        .map(bookResponses -> ResponseEntity.status(HttpStatus.OK).body(bookResponses));
  }

}
