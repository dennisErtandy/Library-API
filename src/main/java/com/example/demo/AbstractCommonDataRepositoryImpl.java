package com.example.demo;

import com.example.demo.CommonDataRepository;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public abstract class AbstractCommonDataRepositoryImpl<T> implements CommonDataRepository<T> {
  private static final Logger log = LoggerFactory.getLogger(AbstractCommonDataRepositoryImpl.class);
  protected static final String ID_FIELD = "_id";
  protected final ReactiveMongoTemplate reactiveMongoTemplate;
  protected final Class<T> clazz;

  public AbstractCommonDataRepositoryImpl(ReactiveMongoTemplate reactiveMongoTemplate, Class<T> clazz) {
    this.reactiveMongoTemplate = reactiveMongoTemplate;
    this.clazz = clazz;
  }

  public Mono<T> insert(T obj) {
    return this.reactiveMongoTemplate.insert(obj);
  }

  public Mono<T> save(T obj) {
    return this.reactiveMongoTemplate.save(obj);
  }

  protected Mono<T> findById(String id, ReactiveMongoTemplate otherReactiveMongoTemplate) {
    return otherReactiveMongoTemplate.findById(id, this.clazz);
  }

  public Mono<T> findById(String id) {
    return this.reactiveMongoTemplate.findById(id, this.clazz);
  }

  protected Flux<T> findAllByIds(Collection<String> ids, ReactiveMongoTemplate otherReactiveMongoTemplate) {
    Query query = new Query(Criteria.where("_id").in(ids));
    return this.findAllByQuery(otherReactiveMongoTemplate, query);
  }

  public Flux<T> findAllByIds(Collection<String> ids) {
    Query query = new Query(Criteria.where("_id").in(ids));
    return this.findAllByQuery(query);
  }

  public Mono<Boolean> existsById(String id) {
    Query query = new Query(Criteria.where("_id").is(id));
    return this.existsByQuery(query);
  }

  protected Mono<Boolean> existsById(String id, ReactiveMongoTemplate otherReactiveMongoTemplate) {
    Query query = new Query(Criteria.where("_id").is(id));
    return this.existsByQuery(otherReactiveMongoTemplate, query);
  }

  public Mono<Void> delete(T obj) {
    return this.reactiveMongoTemplate.remove(obj).flatMap((deleteResult) -> {
      return deleteResult.wasAcknowledged() && deleteResult.getDeletedCount() == 0L ? Mono.error(new OptimisticLockingFailureException("delete: The entity cannot be deleted! Was it modified or deleted in the meantime?")) : Mono.empty();
    });
  }

  public Mono<Boolean> deleteById(String id) {
    Query query = new Query(Criteria.where("_id").is(id));
    return this.reactiveMongoTemplate.remove(query, this.clazz).flatMap((deleteResult) -> {
      return Mono.just(deleteResult.wasAcknowledged() && deleteResult.getDeletedCount() == 1L);
    }).switchIfEmpty(Mono.defer(() -> {
      return Mono.just(false);
    }));
  }

  protected Mono<Long> count(ReactiveMongoTemplate otherReactiveMongoTemplate) {
    return otherReactiveMongoTemplate.count(new Query(), this.clazz);
  }

  public Mono<Long> count() {
    return this.count(this.reactiveMongoTemplate);
  }

  protected Mono<T> findByQuery(ReactiveMongoTemplate otherReactiveMongoTemplate, Query query) {
    return otherReactiveMongoTemplate.findOne(query, this.clazz);
  }

  protected Mono<T> findByQuery(Query query) {
    return this.reactiveMongoTemplate.findOne(query, this.clazz);
  }

  protected Flux<T> findAllByQuery(ReactiveMongoTemplate otherReactiveMongoTemplate, Query query) {
    return otherReactiveMongoTemplate.find(query, this.clazz);
  }

  protected Flux<T> findAllByQuery(Query query) {
    return this.reactiveMongoTemplate.find(query, this.clazz);
  }

  protected Mono<Long> countByQuery(ReactiveMongoTemplate otherReactiveMongoTemplate, Query query) {
    return otherReactiveMongoTemplate.count(query, this.clazz);
  }

  protected Mono<Long> countByQuery(Query query) {
    return this.reactiveMongoTemplate.count(query, this.clazz);
  }

  protected Mono<Boolean> existsByQuery(ReactiveMongoTemplate otherReactiveMongoTemplate, Query query) {
    return otherReactiveMongoTemplate.exists(query, this.clazz);
  }

  protected Mono<Boolean> existsByQuery(Query query) {
    return this.reactiveMongoTemplate.exists(query, this.clazz);
  }
}
