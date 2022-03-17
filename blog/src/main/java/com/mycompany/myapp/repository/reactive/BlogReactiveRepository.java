package com.mycompany.myapp.repository.reactive;

import com.mycompany.myapp.domain.Blog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the Blog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlogReactiveRepository extends ReactiveMongoRepository<Blog, String> {
}
