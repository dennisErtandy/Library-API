package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RunWith(MockitoJUnitRunner.class)
@WebFluxTest(controllers = BookController.class)
class BookControllerTest {

  private static final String BASE_ENDPOINT = "/api";
  private static final String BOOK_ENDPOINT = "/books";
  private static final String ISBN_ENDPOINT = "/isbn/";
  private static final String ID_ENDPOINT = "/id/";

  private static final String TITLE = "Test Title";
  private static final String ID = "62f49031045a8852275d4686";
  private static final String ISBN = "ISBN 274363937";
  private static final double PRICE = 15000.0;


  final String BOOK_JSON_DATA = "{\"id\":\"" + ID
      + "\",\"title\":\"" + TITLE + "\",\"isbn\":\"" + ISBN
      + "\",\"price\":" + PRICE + "}";
  @MockBean
  BookService bookService;
  @Autowired
  private WebTestClient webTestClient;

  private static Book initBookObject() {
    return Book.builder().id(ID).title(TITLE).isbn(ISBN).price(PRICE).build();
  }

  private static CreateBookRequest initCreateBookRequestObject() {
    return CreateBookRequest.builder().title(TITLE).isbn(ISBN).price(PRICE).build();
  }

  private String initResponse(String status, String message, boolean dataExist) {
    String dataPayload;
    dataPayload = dataExist ? BOOK_JSON_DATA : "";
    return "{\"status\":\"" + status + "\",\"message\":\""
        + message + "\",\"data\":[" + dataPayload + "]}";

  }

  private ResponseTemplate<BookResponse> initTemplateResponseTest(final BookResponse bookresponse,
      String status, String message) {
    return new ResponseTemplate<>(status, message, bookresponse);
  }

  private ResponseTemplate<BookResponse> initSuccessBookResponseTest(final Book book) {
    final ModelMapper modelMapper = new ModelMapper();
    BookResponse bookResponse = modelMapper.map(book, BookResponse.class);
    return initTemplateResponseTest(bookResponse, ResponseTemplate.SUCCESS_STATUS,
        ResponseTemplate.SUCCESS_RETRIEVE_MESSAGE);

  }

  @Test
  void getBooks() {
    Book book = initBookObject();
    when(bookService.getAllBooks()).thenReturn(Flux.just(book));

    webTestClient.get().uri(BASE_ENDPOINT + BOOK_ENDPOINT).accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus().isOk().expectBody()
        .json(
            initResponse(ResponseTemplate.SUCCESS_STATUS, ResponseTemplate.SUCCESS_RETRIEVE_MESSAGE,
                true));
    verify(bookService, times(1)).getAllBooks();

  }

  @Test
  void getBooks_returnEmpty() {
    Book book = initBookObject();
    when(bookService.getAllBooks()).thenReturn(Flux.empty());

    webTestClient.get().uri(BASE_ENDPOINT + BOOK_ENDPOINT).accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus().isOk().expectBody()
        .json(
            initResponse(ResponseTemplate.SUCCESS_STATUS, ResponseTemplate.SUCCESS_RETRIEVE_MESSAGE,
                false));
    verify(bookService, times(1)).getAllBooks();
  }

  @Test
  void createBooks() {
    CreateBookRequest createBookRequest = initCreateBookRequestObject();
    Book book = initBookObject();

    when(bookService.create(any(CreateBookRequest.class))).thenReturn(Mono.just(book));

    webTestClient.post().uri(BASE_ENDPOINT + BOOK_ENDPOINT).accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(createBookRequest), CreateBookRequest.class).exchange().expectStatus()
        .isOk().expectBody()
        .json(
            initResponse(ResponseTemplate.SUCCESS_STATUS, ResponseTemplate.SUCCESS_CREATE_MESSAGE,
                true));
    ArgumentCaptor<CreateBookRequest> argument = ArgumentCaptor.forClass(CreateBookRequest.class);
    verify(bookService, times(1)).create(argument.capture());


  }

  @Test
  void getBooksByIsbn() {
    Book book = initBookObject();

    when(bookService.getBookByIsbn(anyString())).thenReturn(Mono.just(book));

    webTestClient.get().uri(BASE_ENDPOINT + BOOK_ENDPOINT + ISBN_ENDPOINT + ISBN)
        .accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus()
        .isOk().expectBody()
        .json(
            initResponse(ResponseTemplate.SUCCESS_STATUS, ResponseTemplate.SUCCESS_RETRIEVE_MESSAGE,
                true));
    verify(bookService, times(1)).getBookByIsbn(ISBN);

  }

  @Test
  void getBooksByIsbn_returnNotFound() {
    Book book = initBookObject();

    when(bookService.getBookByIsbn(anyString())).thenReturn(Mono.empty());

    webTestClient.get().uri(BASE_ENDPOINT + BOOK_ENDPOINT + ISBN_ENDPOINT + ISBN)
        .accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus()
        .isNotFound();
    verify(bookService, times(1)).getBookByIsbn(ISBN);

  }

  @Test
  void getBooksById() {
    Book book = initBookObject();

    when(bookService.getBookById(anyString())).thenReturn(Mono.just(book));

    webTestClient.get().uri(BASE_ENDPOINT + BOOK_ENDPOINT + ID_ENDPOINT + ID)
        .accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus()
        .isOk().expectBody()
        .json(
            initResponse(ResponseTemplate.SUCCESS_STATUS, ResponseTemplate.SUCCESS_RETRIEVE_MESSAGE,
                true));
    verify(bookService, times(1)).getBookById(ID);
  }

  @Test
  void getBooksById_returnNotFound() {

    when(bookService.getBookById(anyString())).thenReturn(Mono.empty());

    webTestClient.get().uri(BASE_ENDPOINT + BOOK_ENDPOINT + ID_ENDPOINT + ID)
        .accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus()
        .isNotFound();
    verify(bookService, times(1)).getBookById(ID);

  }

  @Test
  void deleteBooks() {
    Book book = initBookObject();

    when(bookService.deleteBook(anyString())).thenReturn(Mono.just(book));

    webTestClient.delete().uri(BASE_ENDPOINT + BOOK_ENDPOINT + ID_ENDPOINT + ID)
        .accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus()
        .isOk().expectBody()
        .json(
            initResponse(ResponseTemplate.SUCCESS_STATUS, ResponseTemplate.SUCCESS_DELETE_MESSAGE,
                true));
    verify(bookService, times(1)).deleteBook(ID);
  }

  @Test
  void deleteBooks_returnNotFound() {
    when(bookService.deleteBook(anyString())).thenReturn(Mono.empty());

    webTestClient.delete().uri(BASE_ENDPOINT + BOOK_ENDPOINT + ID_ENDPOINT + ID)
        .accept(MediaType.APPLICATION_JSON)
        .exchange().expectStatus()
        .isNotFound();
    verify(bookService, times(1)).deleteBook(ID);
  }

  @Test
  void updateBooks() {
    Book book = initBookObject();
    CreateBookRequest createBookRequest = initCreateBookRequestObject();

    when(bookService.updateBook(anyString(), any(CreateBookRequest.class))).thenReturn(
        Mono.just(book));

    webTestClient.put().uri(BASE_ENDPOINT + BOOK_ENDPOINT + ID_ENDPOINT + ID)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(createBookRequest), CreateBookRequest.class)
        .exchange().expectStatus()
        .isOk().expectBody()
        .json(
            initResponse(ResponseTemplate.SUCCESS_STATUS, ResponseTemplate.SUCCESS_UPDATE_MESSAGE,
                true));
    verify(bookService, times(1)).updateBook(anyString(), any(CreateBookRequest.class));
  }

  @Test
  void updateBooks_thenReturnNotFound() {
    CreateBookRequest createBookRequest = initCreateBookRequestObject();

    when(bookService.updateBook(anyString(), any(CreateBookRequest.class))).thenReturn(
        Mono.empty());

    webTestClient.put().uri(BASE_ENDPOINT + BOOK_ENDPOINT + ID_ENDPOINT + ID)
        .accept(MediaType.APPLICATION_JSON)
        .body(Mono.just(createBookRequest), CreateBookRequest.class)
        .exchange().expectStatus()
        .isNotFound();
    verify(bookService, times(1)).updateBook(anyString(), any(CreateBookRequest.class));
  }
}
