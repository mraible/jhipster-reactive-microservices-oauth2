package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.User;


import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * Spring Data MongoDB repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> findOneByEmailIgnoreCase(String email);

    Mono<User> findOneByLogin(String login);

    Flux<User> findAllByLoginNot(Pageable pageable, String login);

    Mono<Long> countAllByLoginNot(String anonymousUser);
}
