package com.example.demo;

import java.util.Collection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

  public interface CommonDataRepository<T> {
    Mono<T> insert(T var1);

    Mono<T> save(T var1);

    Mono<T> findById(String var1);

    Flux<T> findAllByIds(Collection<String> var1);

    Mono<Boolean> existsById(String var1);

    Mono<Void> delete(T var1);

    Mono<Boolean> deleteById(String var1);

    Mono<Long> count();
  }


