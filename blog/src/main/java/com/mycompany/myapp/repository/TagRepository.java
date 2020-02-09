package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Tag;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Tag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRepository extends MongoRepository<Tag, String> {

}
