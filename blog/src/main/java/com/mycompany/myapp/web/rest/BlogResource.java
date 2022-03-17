package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Blog;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.repository.reactive.BlogReactiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Blog}.
 */
@RestController
@RequestMapping("/api")
public class BlogResource {

    private final Logger log = LoggerFactory.getLogger(BlogResource.class);

    private final BlogReactiveRepository blogRepository;

    private final UserRepository userRepository;

    public BlogResource(BlogReactiveRepository blogRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@code GET  /blogs} : get all the blogs as a stream.
     *
     * @return the {@link Flux} of blogs.
     */
    @GetMapping(value = "/blogs", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Blog> getAllBlogsAsStream() {
        log.debug("REST request to get all Blogs as a stream");
        return blogRepository.findAll();
    }
}
