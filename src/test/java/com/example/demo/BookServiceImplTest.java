package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Flux;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class BookServiceImplTest {

  private static final String TITLE = "Test Title";
  private static final String ID = "62f49031045a8852275d4686";
  private static final String ISBN = "ISBN 274363937";
  private static final double PRICE = 15000.0;
  @MockBean
  BookRepository bookRepository;

  @Autowired
  BookService bookService;

  private static CreateBookRequest initCreateBookRequestObject() {
    return CreateBookRequest.builder().title(TITLE).isbn(ISBN).price(PRICE).build();
  }

  private static Book initBookObject() {
    return Book.builder().id(ID).title(TITLE).isbn(ISBN).price(PRICE).build();
  }

  @Test
  void getAllBooks() {
    Book book = initBookObject();
    given(bookRepository.getAllBooks()).willReturn(Flux.just(book, book));
    StepVerifier.create(bookService.getAllBooks().collectList())
        .consumeNextWith(bookEntityStream -> {
          bookEntityStream.forEach(bookEntity -> {
            Assert.assertEquals(ID, bookEntity.getId());
            Assert.assertEquals(PRICE, bookEntity.getPrice(), 0.001);
            Assert.assertEquals(ISBN, bookEntity.getIsbn());
            Assert.assertEquals(TITLE, bookEntity.getTitle());

          });
        });
    verify(bookRepository, times(1)).getAllBooks();

  }

  @Test
  void getBookById() {
    Book book = initBookObject();
    given(bookRepository.findBookById(ID)).willReturn(Mono.just(book));
    StepVerifier.create(bookService.getBookById(ID)).expectNext(book).verifyComplete();
    verify(bookRepository, times(1)).findBookById(ID);
  }

  @Test
  void getBookByIsbn() {
    Book book = initBookObject();
    given(bookRepository.findBookByIsbn(ISBN)).willReturn(Mono.just(book));
    StepVerifier.create(bookService.getBookByIsbn(ISBN)).expectNext(book).verifyComplete();
    verify(bookRepository, times(1)).findBookByIsbn(ISBN);
  }

  @Test
  void create() {
    Book book = initBookObject();
    CreateBookRequest createBookRequest = initCreateBookRequestObject();
    given(bookRepository.insert(any(Book.class))).willReturn(Mono.just(book));
    StepVerifier.create(bookService.create(createBookRequest))
        .expectNextMatches(bookEntity -> bookEntity.equals(book)).verifyComplete();
    verify(bookRepository, times(1)).insert(any(Book.class));
  }

  @Test
  void deleteBook() {
    Book book = initBookObject();
    given(bookRepository.deleteBookById(ID)).willReturn(Mono.just(book));
    StepVerifier.create(bookService.deleteBook(ID)).expectNext(book).verifyComplete();
    verify(bookRepository, times(1)).deleteBookById(ID);
  }

  @Test
  void updateBook() {
    Book book = initBookObject();
    CreateBookRequest createBookRequest = initCreateBookRequestObject();

    given(bookRepository.updateBookById(ID, createBookRequest)).willReturn(Mono.just(book));
    StepVerifier.create(bookService.updateBook(ID, createBookRequest)).expectNext(book)
        .verifyComplete();
    verify(bookRepository, times(1)).updateBookById(ID, createBookRequest);

  }
}
