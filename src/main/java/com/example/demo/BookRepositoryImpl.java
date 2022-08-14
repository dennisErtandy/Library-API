package com.example.demo;

import com.example.demo.Book.Fields;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class BookRepositoryImpl extends AbstractCommonDataRepositoryImpl<Book> implements
    BookRepository {

  private static final String ISBN = "isbn";
  private static final String ID = "_id";

  private static final String DELETED_AT = "deletedAt";
  @Autowired
  private ReactiveMongoTemplate reactiveMongoTemplate;

  @Autowired
  public BookRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate) {
    super(reactiveMongoTemplate, Book.class);
    this.reactiveMongoTemplate = reactiveMongoTemplate;
  }

  @Override
  public Flux<Book> getAllBooks() {
    final Query query = new Query().addCriteria(Criteria.where(DELETED_AT).isNull());

    return reactiveMongoTemplate.find(query, Book.class);
  }

  @Override
  public Mono<Book> findBookByIsbn(String isbn) {
    final Query query = new Query().addCriteria(
        Criteria.where(ISBN).is(isbn).and(DELETED_AT).isNull());
    return reactiveMongoTemplate.findOne(query, Book.class);
  }

  @Override
  public Mono<Book> findBookById(String id) {
    final Query query = new Query().addCriteria(Criteria.where(ID).is(id).and(DELETED_AT).isNull());
    return reactiveMongoTemplate.findOne(query, Book.class);
  }

  @Override
  public Mono<Book> deleteBookById(String id) {
    final Query query = new Query().addCriteria(Criteria.where(ID).is(id));

    final Update update = new Update().set(Book.Fields.deletedAt, LocalDateTime.now());
    return reactiveMongoTemplate.findAndModify(query, update, Book.class);
  }

  @Override
  public Mono<Book> updateBookById(String id, CreateBookRequest createBookRequest) {
    final Query query = new Query().addCriteria(Criteria.where(ID).is(id));
    final Update update = new Update().set(Fields.title, createBookRequest.getTitle())
        .set(Fields.price, createBookRequest.getPrice())
        .set(Fields.isbn, createBookRequest.getIsbn());
    FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);
    return reactiveMongoTemplate.findAndModify(query, update, findAndModifyOptions, Book.class);
  }

}
